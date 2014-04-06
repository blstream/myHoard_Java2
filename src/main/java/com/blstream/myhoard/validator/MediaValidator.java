/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blstream.myhoard.validator;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.MediaDTO;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author gohilukk
 */
public class MediaValidator {

    private ResourceService<MediaDTO> mediaService;

    public void setMediaService(ResourceService<MediaDTO> mediaService) {
        this.mediaService = mediaService;
    }

    public void validateGet(HttpServletRequest request, String id) {
        UserDTO user = (UserDTO) request.getAttribute("user");
        MediaDTO media = mediaService.get(Integer.parseInt(id));
        if (!user.getId().equals(media.getOwner())) {
            throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak uprawnień do zasobu.");
        }
    }

    public void validatePost(MultipartHttpServletRequest request) {
        MultiValueMap<String, MultipartFile> map = request.getMultiFileMap();
        if (map.isEmpty() || !map.values().iterator().next().iterator().hasNext()) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Brak pliku");
        }
        if (map.size() != 1) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Można wrzucać tylko 1 plik na raz");
        }
        MultipartFile file = map.values().iterator().next().iterator().next();
        try {
            if (ImageIO.read(file.getInputStream()) == null)
                throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Uszkodzony plik");
        } catch (IOException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Uszkodzony plik");
        }
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Niepoprawny plik, to nie jest zdjęcie");
        }
        if (file.getSize() == 0) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Próba zapisania pustego pliku");
        }
    }

    public byte[] validatePut(HttpServletRequest request, String id) {
        try {
            UserDTO user = (UserDTO) request.getAttribute("user");
            MediaDTO media = mediaService.get(Integer.parseInt(id));
            if (!user.getId().equals(media.getOwner())) {
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak uprawnień do zasobu.");
            }
            List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            if (multiparts.isEmpty())
                throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Brak pliku");
            if (multiparts.size() != 1)
                throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Można aktualizować tylko 1 plik na raz");
            FileItem file = multiparts.get(0);
            if (multiparts.get(0).getSize() == 0 || !multiparts.get(0).getContentType().contains("image")) {
                throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Niepoprawny plik");
            }
            try {
                if (ImageIO.read(file.getInputStream()) == null)
                    throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Uszkodzony plik");
            } catch (IOException ex) {
                throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Uszkodzony plik");
            }
            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Niepoprawny plik, to nie jest zdjęcie");
            }
            if (file.getSize() == 0) {
                throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Próba zapisania pustego pliku");
            }
            return IOUtils.toByteArray(file.getInputStream());
        } catch (FileUploadException | IOException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Uszkodzony plik");
        }
    }

    public void validateDelete(HttpServletRequest request, String id) {
        UserDTO user = (UserDTO) request.getAttribute("user");
        MediaDTO media = mediaService.get(Integer.parseInt(id));
        if (!user.getId().equals(media.getOwner())) {
            throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak uprawnień do zasobu.");
        }
    }
}
