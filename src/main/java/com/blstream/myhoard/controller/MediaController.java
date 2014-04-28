package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.MediaDTO;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.MediaService;
import com.blstream.myhoard.validator.MediaValidator;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author gohilukk
 */
@Controller
@RequestMapping(value = "/media")
public class MediaController {
    
//    @Autowired
    private MediaService mediaService;
    
    private MediaValidator mediaValidator;
    
    public void setMediaValidator(MediaValidator mediaValidator) {
        this.mediaValidator = mediaValidator;
    }

    public void setMediaService(MediaService mediaService) {
        this.mediaService = mediaService;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MediaDTO> getMedia(HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getAttribute("user");
        Map<String, Object> params = new HashMap<>();
        params.put("owner", Integer.parseInt(user.getId()));
        return mediaService.getList(params);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {"image/jpeg", "image/png", "image/gif"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public byte[] get(@PathVariable String id, @RequestParam(value = "size", defaultValue = "0") String size,
            HttpServletRequest request) {
        mediaValidator.validateGet(request, id);
        return mediaService.getThumbnail(Integer.parseInt(id), Integer.parseInt(size));
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public MediaDTO addMedia(MultipartHttpServletRequest request) {
        mediaValidator.validatePost(request);
        try {
            MediaDTO media = new MediaDTO();
            UserDTO user = (UserDTO) request.getAttribute("user");
            MultiValueMap<String, MultipartFile> map = request.getMultiFileMap();
            MultipartFile file = map.values().iterator().next().iterator().next();
            media.setFile(file.getBytes());
            media.setOwner(user.getId());
            mediaService.create(media);
            return media;
        } catch (IOException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Niepoprawny plik");
        }
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMedia(@PathVariable String id, HttpServletRequest request) {
        mediaValidator.validateDelete(request, id);
        mediaService.remove(Integer.parseInt(id));
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MediaDTO updateMedia(@PathVariable String id, HttpServletRequest request) {
        byte[] file = mediaValidator.validatePut(request, id);
        MediaDTO media = mediaService.get(Integer.parseInt(id));
        media.setFile(file);
        mediaService.update(media);
        return media;
    }
    
    @RequestMapping(value = "/{id}/thumbnailShow", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void getThumbnailShowSize(@PathVariable String id, @RequestParam(value = "size", defaultValue = "0") String size,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            UserDTO user = (UserDTO) request.getAttribute("user");
            MediaDTO media = mediaService.get(Integer.parseInt(id));
            if (!user.getId().equals(media.getOwner())) {
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak uprawnień do zasobu.");
            }
            byte[] imageBytes = mediaService.getThumbnail(Integer.parseInt(id), Integer.parseInt(size));
            response.setContentType("image/jpeg");
            response.setContentLength(imageBytes.length);
            response.getOutputStream().write(imageBytes);
        } catch (IOException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Niepoprawny plik");
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("id", "Niepoprawny identyfikator.");
        }
    }
    
    @ExceptionHandler(MyHoardException.class)
    public void returnCode(MyHoardException exception, HttpServletResponse response) throws IOException {
        // @ResponseBody odmawiało posłuszeństwa z niewyjaśnionych powodów
        response.setStatus(exception.getResponseStatus());
        response.setContentType("application/json;charset=UTF-8");
        response.getOutputStream().write(exception.toError().toString().getBytes("UTF-8"));
    }
}
