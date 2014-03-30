package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.MyHoardError;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.CollectionDTO;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import com.blstream.myhoard.db.model.SortResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.hibernate.HibernateException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/collections")
public class CollectionController {

    private ResourceService<CollectionDTO> collectionService;

    public void setCollectionService(ResourceService<CollectionDTO> collectionService) {
        this.collectionService = collectionService;
    }

    /**
     * Obsługa wypisywania/sortowania kolekcji.
     * @param fieldName - po jakich polach sortować (domyślnie po nazwie)
     * @param sortDir   - w jakim kierunku ("asc" lub "desc"; domyślnie "asc")
     * @return Lista kolekcji.
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<CollectionDTO> getCollections(@RequestParam(value = "sort_by", defaultValue = "name") String[] fieldName,
            @RequestParam(value = "sort_direction", defaultValue = "asc") String sortDir, HttpServletRequest request) {
        UserDTO user =(UserDTO) request.getAttribute("user");
        Map<String, Object> params = new HashMap<>();
        params.put("sort_by", fieldName);
        params.put("sort_dir", sortDir);
        params.put("owner", user.getUsername());
        return collectionService.getList(params);
    }

    /**
     * Obsługa stronicowania.
     * @param maxCount - ile elementów wypisać
     * @param startNum - od którego elementu zacząć
     * @return Wynik stronicowania.
     */
    @RequestMapping(method = RequestMethod.GET, params = {"max_count", "start_num"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SortResult listCollections(@RequestParam(value = "max_count", defaultValue = "2147483647") Integer maxCount,
            @RequestParam(value = "start_num", defaultValue = "0") String startNum) {
        Map<String, Object> params = new HashMap<>();
        SortResult result = new SortResult();
        params.put("max_count", maxCount);
        params.put("start_num", startNum);
        result.setCollections(collectionService.getList(params));
        result.setTotalCount(result.getCollections().size());
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CollectionDTO addCollection(@RequestBody @Valid CollectionDTO collection, BindingResult result,HttpServletRequest request) {
        if (result.hasErrors())
            throw new MyHoardException(201, "Validation error").add(result.getFieldError().getField(), result.getFieldError().getDefaultMessage());
        UserDTO user = (UserDTO)request.getAttribute("user");
        collection.setOwner(user.getUsername());
        collectionService.create(collection);
        return collection;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CollectionDTO getCollection(@PathVariable String id, HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            CollectionDTO collection = collectionService.get(Integer.parseInt(id));
            if (collection.getOwner().equals(user.getUsername()))
                return collection;
            else
                throw new MyHoardException(104, "Forbidden", HttpServletResponse.SC_FORBIDDEN).add("id", "Brak uprawnień do zasobu.");
        } catch (NumberFormatException ex) {
            throw new MyHoardException(201, "Validation error").add("id", "Niepoprawny identyfikator.");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CollectionDTO updateCollection(@PathVariable String id, @Valid @RequestBody CollectionDTO collection, BindingResult result, HttpServletRequest request) {
        if (collection.getName() != null && result.hasFieldErrors("name") || collection.getDescription() != null && result.hasFieldErrors("description")) {
            FieldError error = collection.getName() != null ? result.getFieldError("name") : result.getFieldError("description");
            throw new MyHoardException(201, "Validation error").add(error.getField(), error.getDefaultMessage());
        }
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            CollectionDTO tmp = collectionService.get(Integer.parseInt(id));
            if (tmp.getOwner().equals(user.getUsername())) {
                collection.setId(id);
                collectionService.update(collection);
                return collection;
            } else
                throw new MyHoardException(104, "Forbidden", HttpServletResponse.SC_FORBIDDEN).add("id", "Brak uprawnień do zasobu.");
        } catch (NumberFormatException ex) {
            throw new MyHoardException(201, "Validation error").add("id", "Niepoprawny identyfikator.");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCollection(@PathVariable String id, HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            CollectionDTO tmp = collectionService.get(Integer.parseInt(id));
            if (tmp.getOwner().equals(user.getUsername()))
                collectionService.remove(Integer.parseInt(id));
            else
                throw new MyHoardException(104, "Forbidden", HttpServletResponse.SC_FORBIDDEN).add("id", "Brak uprawnień do zasobu.");
        } catch (NumberFormatException ex) {
            throw new MyHoardException(201, "Validation error").add("id", "Niepoprawny identyfikator.");
        }
    }

    @ExceptionHandler(MyHoardException.class)
    @ResponseBody
    public MyHoardError returnCode(MyHoardException exception, HttpServletResponse response) {
        response.setStatus(exception.getResponseStatus());
        return exception.toError();
    }
}
