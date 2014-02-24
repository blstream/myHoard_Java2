package com.blstream.myhoard.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.blstream.myhoard.biz.service.CollectionService;
import com.blstream.myhoard.biz.exception.CollectionException;
import com.blstream.myhoard.biz.model.*;
import com.blstream.myhoard.validation.*;


@Controller 
@RequestMapping("/collections") 
public class CollectionController {

    @Autowired
	CollectionService collectionService;

    @Autowired
    CollectionDTOValidator collectionDTOValidator;
    
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<CollectionDTO> getCollections() {
		return collectionService.getList();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED) 	
	public @ResponseBody CollectionDTO addCollection(@RequestBody CollectionDTO collection, BindingResult result) {
	    collectionDTOValidator.validate(collection, result);
        if (result.hasErrors()) {
            //jakis error
            throw new CollectionException("blad");
        } else {
            collectionService.create(collection);
            return collection;
        }
	}

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody CollectionDTO getCollection(@PathVariable String id) {
		// TODO dla dowolnego ID (którego nie ma w bazie) listuje wszystkie kolekcje
	    try {
	        int idd = Integer.parseInt(id);
	        return collectionService.get(idd);	        
	    }
	    catch (Exception ex) {
            throw new CollectionException("blad");	        
	    }
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody CollectionDTO updateCollection(@PathVariable String id, @RequestBody CollectionDTO obj) {
		// TODO czasem nie wyłapuje metody - trzeba by weryfikować, czy istnieje obiekt o podanym ID
        try {
            Integer.parseInt(id);
            obj.setId(id);
            collectionService.update(obj);
            return obj;            
        }
        catch (Exception ex) {
            throw new CollectionException("blad");          
        }	    
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeCollection(@PathVariable String id) {
        try {
            int idd = Integer.parseInt(id);
            collectionService.remove(idd);            
        }
        catch (Exception ex) {
            throw new CollectionException("blad");          
        }	    
	}

//	@ExceptionHandler(value = {NumberFormatException.class, IndexOutOfBoundsException.class, StaleStateException.class})
	@ExceptionHandler(value = Exception.class)	// łapie wszystkie wyjątki
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String exceptionHandler() {
		// TODO
		return "";
	}
}
