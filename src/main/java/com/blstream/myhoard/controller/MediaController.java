package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardError;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.MediaDTO;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.MediaService;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestParam;
/**
 *
 * @author gohilukk
 */
@Controller
@RequestMapping(value = "/media")
public class MediaController extends HttpServlet {

    @Autowired
    private MediaService mediaService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MediaDTO> getMedia(HttpServletRequest request) {
        UserDTO user = (UserDTO)request.getAttribute("user");
        Map<String, Object> params = new HashMap<>();
        params.put("owner", user.getUsername());
        return mediaService.getList(params);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {"image/jpeg", "image/png", "image/gif"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public byte[] get(@PathVariable String id, @RequestParam(value = "size", defaultValue = "0") String size,
            HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            MediaDTO media = mediaService.get(Integer.parseInt(id));
            if (!user.getUsername().equals(media.getOwner()))
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak uprawnień do zasobu.");
            if ("0".equals(size))
                return media.getFile();
            else
                return mediaService.getThumbnail(Integer.parseInt(id), Integer.parseInt(size));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("id", "Niepoprawny identyfikator.");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public MediaDTO addMedia(MultipartFile file, HttpServletRequest request) {
        if (file.getSize() == 0 || !file.getContentType().contains("image"))
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Niepoprawny plik");
        MediaDTO media = new MediaDTO();
        UserDTO user = (UserDTO)request.getAttribute("user");
        media.setOwner(user.getUsername());
        try {
            media.setFile(file.getBytes());
            mediaService.create(media);
            return media;
        } catch (IOException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Niepoprawny plik");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMedia(@PathVariable String id, HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            MediaDTO media = mediaService.get(Integer.parseInt(id));
            if (!user.getUsername().equals(media.getOwner()))
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak uprawnień do zasobu.");
            mediaService.remove(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("id", "Niepoprawny identyfikator.");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MediaDTO updateMedia(@PathVariable String id, HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            MediaDTO media = mediaService.get(Integer.parseInt(id));
            if (!user.getUsername().equals(media.getOwner()))
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak uprawnień do zasobu.");
            InputStream file;
            List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            if (multiparts.get(0).getSize() == 0 || !multiparts.get(0).getContentType().contains("image"))
                throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Niepoprawny plik");
            file = multiparts.get(0).getInputStream();
            media.setFile(IOUtils.toByteArray(file));
            mediaService.update(media);
            return media;
        } catch (IOException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Niepoprawny plik");
        } catch (FileUploadException ex) {
            throw new MyHoardException(ErrorCode.INTERNAL_SERVER_ERROR).add("file", "Problem z zapisem pliku do bazy");
        }
    }

    @RequestMapping(value = "/{id}/thumbnailShow", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void getThumbnailShowSize(@PathVariable String id, @RequestParam(value = "size", defaultValue = "0") String size,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            MediaDTO media = mediaService.get(Integer.parseInt(id));
            if (!user.getUsername().equals(media.getOwner()))
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak uprawnień do zasobu.");
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
        response.getOutputStream().write(exception.toError().toString().getBytes());
    }
}
