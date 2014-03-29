package com.blstream.myhoard.controller;

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
import org.hibernate.HibernateException;
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
    public List<MediaDTO> getMedia(HttpServletRequest requeest) {
        try {
            UserDTO user = (UserDTO)requeest.getAttribute("user");
            Map<String, Object> params = new HashMap<>();
            params.put("owner", user.getUsername());
            return mediaService.getList(params);
        } catch (MyHoardException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MyHoardException(400);
        }
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
                throw new MyHoardException(104, "Forbidden", HttpServletResponse.SC_FORBIDDEN);
            if ("0".equals(size))
                return media.getFile();
            else
                return mediaService.getThumbnail(Integer.parseInt(id), Integer.parseInt(size));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(201, "Validation error").add("id", "Niepoprawny identyfikator");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    MediaDTO addMedia(MultipartFile file) throws IOException {
        if (file.getSize() == 0 || !file.getContentType().contains("image"))
            throw new MyHoardException(201, "Validation error").add("file", "Niepoprawny plik");
        MediaDTO media = new MediaDTO();
        try {
            media.setFile(file.getBytes());
            mediaService.create(media);
            return media;
        } catch (IOException ex) {
            throw new MyHoardException(400, ex.toString());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMedia(@PathVariable String id, HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            MediaDTO media = mediaService.get(Integer.parseInt(id));
            if (!user.getUsername().equals(media.getOwner()))
                throw new MyHoardException(104, "Forbidden", HttpServletResponse.SC_FORBIDDEN);
            mediaService.remove(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(201, "Validation error").add("id", "Niepoprawny identyfikator");
        } catch (HibernateException ex) {
            throw new MyHoardException(400, ex.toString());
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
                throw new MyHoardException(104, "Forbidden", HttpServletResponse.SC_FORBIDDEN);
            InputStream file;
            List<FileItem> multiparts = new ServletFileUpload(
                                         new DiskFileItemFactory()).parseRequest(request);
            file = multiparts.get(0).getInputStream();
//            MediaDTO m = mediaService.get(Integer.parseInt(id));
//            media.setId(id); //poniewaz konstruktor daje id=0;
//            media.setItem(m.getItem());
            media.setFile(IOUtils.toByteArray(file));
            mediaService.update(media);
            return media;
        } catch (IOException ex) {
            throw new MyHoardException(400, ex.toString());
        } catch (FileUploadException ex) {
            throw new MyHoardException(400, ex.toString());
        }
    }

    @RequestMapping(value = "/{id}/thumbnailShow", method = RequestMethod.GET, produces = {"image/jpeg", "image/png", "image/gif"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void getThumbnailShowSize(@PathVariable String id, @RequestParam(value = "size", defaultValue = "0") String size,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            MediaDTO media = mediaService.get(Integer.parseInt(id));
            if (!user.getUsername().equals(media.getOwner()))
                throw new MyHoardException(104, "Forbidden", HttpServletResponse.SC_FORBIDDEN);
            byte[] imageBytes = mediaService.getThumbnail(Integer.parseInt(id), Integer.parseInt(size));

            response.setContentType("image/jpeg");
            response.setContentLength(imageBytes.length);

            response.getOutputStream().write(imageBytes);
        } catch (NumberFormatException | IOException ex) {
            throw new MyHoardException(201, ex.toString());
        }
    }

    @ExceptionHandler(MyHoardException.class)
    @ResponseBody
    public MyHoardError returnCode(MyHoardException exception, HttpServletResponse response) {
        response.setStatus(exception.getResponseStatus());
        return exception.toError();
        // czasem się wysypywało, stąd poniższy kod - może uszczelnienie wyłapywania wyjątków pomoże
//        response.setHeader("Content-Type", "applcation/json; charset=\"UTF-8\"");
//        MyHoardError error = exception.toError();
//        try {
//            response.getWriter().format("{\n  \"error_code\": %d,\n  \"error_message\": \"%s\"\n}", error.getErrorCode(), error.getErrorReason());
//        } catch (IOException ex) {
//            //
//        }
    }
}
