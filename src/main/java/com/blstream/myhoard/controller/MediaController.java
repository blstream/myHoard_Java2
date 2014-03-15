package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.MediaDTO;
import com.blstream.myhoard.biz.service.MediaService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.*;
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
    public @ResponseBody
    List<MediaDTO> getMedia() {
        return mediaService.getList();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    byte[] getMedia(@PathVariable String id) {
        try {
            return mediaService.get(Integer.parseInt(id)).getFile();
        } catch (Exception ex) {
            throw new MyHoardException(300);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    MediaDTO addMedia(MultipartFile file) throws IOException {
        if (file.getSize() == 0 || !file.getContentType().contains("image")) {
            throw new MyHoardException(400);
        }
        MediaDTO media = new MediaDTO();
        try {
            media.setFile(file.getBytes());
            mediaService.create(media);
            return media;
        } catch (IOException ex) {
            Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
            throw new MyHoardException(400);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMedia(@PathVariable String id) {
        try {
            mediaService.remove(Integer.parseInt(id));
        } catch (Exception ex) {
            throw new MyHoardException(400);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    MediaDTO updateMedia(@PathVariable String id, MultipartFile file) {
        if (file.getSize() == 0 || !file.getContentType().contains("image")) {
            throw new MyHoardException(400);
        }
        try {
            MediaDTO media = new MediaDTO();
            media.setId(id); //poniewaz konstruktor daje id=0;
            media.setFile(file.getBytes());
            mediaService.update(media);
            return media;
        } catch (Exception ex) {
            throw new MyHoardException(111);
        }
    }

    @RequestMapping(value = "/{id}/thumbnail", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    byte[] getThumbnail(@PathVariable String id) {
        try {
            return mediaService.get(Integer.parseInt(id)).getThumbnail();
        } catch (Exception ex) {
            throw new MyHoardException(300);
        }
    }

    @RequestMapping(value = "/{id}/thumbnailShow", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    void getThumbnailShow(@PathVariable String id, HttpServletResponse response) {
        try {
            byte[] imageBytes = mediaService.get(Integer.parseInt(id)).getThumbnail();

            response.setContentType("image/jpeg");
            response.setContentLength(imageBytes.length);

            response.getOutputStream().write(imageBytes);
        } catch (Exception ex) {
            throw new MyHoardException(300);
        }
    }

    @ExceptionHandler(MyHoardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ErrorCode returnCode(MyHoardException exception) {
        return new ErrorCode(exception.getErrorCode());
    }
}
