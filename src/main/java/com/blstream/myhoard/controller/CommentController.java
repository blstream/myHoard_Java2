/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardError;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.CollectionDTO;
import com.blstream.myhoard.biz.model.CommentDTO;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author gohilukk
 */
@Controller
//@RequestMapping(value = "/comments")
public class CommentController {

    private ResourceService<CommentDTO> commentService;
    private ResourceService<CollectionDTO> collectionService;

    public void setCommentService(ResourceService<CommentDTO> commentService) {
        this.commentService = commentService;
    }

    public void setCollectionService(ResourceService<CollectionDTO> collectionService) {
        this.collectionService = collectionService;
    }    
    
    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<CommentDTO> getComments(HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getAttribute("user");
        Map<String, Object> params = new HashMap<>();
        params.put("owner", user.toUserDS());
        return commentService.getList(params);
    }

    @RequestMapping(value = "/comments/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommentDTO getComment(@PathVariable String id, HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getAttribute("user");
        CommentDTO comment = commentService.get(Integer.parseInt(id));
        if (comment.getOwner().equals(user)) {
            return comment;
        } else {
            throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak uprawnień do zasobu.");
        }
    }

    @RequestMapping(value = "/collections/{id}/comments", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<CommentDTO> getCommentsOfSpecificCollection(@PathVariable String id, HttpServletRequest request) {
        UserDTO user = (UserDTO)request.getAttribute("user");
        Map<String, Object> params = new HashMap<>();
        params.put("collection", Integer.parseInt(id));
        //params.put("owner", user.toUserDS());
        return commentService.getList(params);
    }    
    
    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CommentDTO addComment(@RequestBody @Valid CommentDTO comment, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add(result.getFieldError().getField(), result.getFieldError().getDefaultMessage());
        }
        UserDTO user = (UserDTO) request.getAttribute("user");
        comment.setOwner(user);
        commentService.create(comment);
        return comment;
    }

    @RequestMapping(value = "/comments/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommentDTO updateItem(@PathVariable String id, @Valid @RequestBody CommentDTO obj, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add(result.getFieldError().getField(), result.getFieldError().getDefaultMessage());
        }
        CommentDTO commentDTO = commentService.get(Integer.parseInt(id));
        UserDTO user = (UserDTO) request.getAttribute("user");
        obj.setOwner(user);
        obj.setId(id);
        obj.setCreatedDate(commentDTO.getCreatedDate());
        if (obj.getContent() == null) {
            obj.setContent(commentDTO.getContent());
        }
        if (obj.getCollection() == null) {
            obj.setCollection(commentDTO.getCollection());
        }
        commentService.update(obj);
        return obj;
    }

    @RequestMapping(value = "/comments/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCollection(@PathVariable String id, HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO) request.getAttribute("user");
            CommentDTO commentDTO = commentService.get(Integer.parseInt(id));
            CollectionDTO collectionDTO = collectionService.get(Integer.parseInt(commentDTO.getCollection()));
            if (commentDTO.getOwner().equals(user) || collectionDTO.getOwner().equals(user)) {
                commentService.remove(Integer.parseInt(id));
            } else {
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak uprawnień do zasobu.");
            }
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("id", "Niepoprawny identyfikator.");
        }
    }

    @ExceptionHandler(MyHoardException.class)
    @ResponseBody
    public MyHoardError returnCode(MyHoardException exception, HttpServletResponse response) {
        response.setStatus(exception.getResponseStatus());
        response.setContentType("application/json;charset=UTF-8");
        return exception.toError();
    }
}
