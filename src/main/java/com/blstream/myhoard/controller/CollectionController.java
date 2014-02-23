package com.blstream.myhoard.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.blstream.myhoard.biz.service.CollectionService;
import com.blstream.myhoard.biz.model.*;
import org.hibernate.StaleStateException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller 
@RequestMapping("/collections") 
public class CollectionController {

    @Autowired
	CollectionService collectionService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<CollectionDTO> getCollections() {
		return collectionService.getList();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED) 	
	public @ResponseBody CollectionDTO addCollection(@RequestBody CollectionDTO obj) {
		collectionService.create(obj);
		return obj;
	}

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody CollectionDTO getCollection(@PathVariable String id) {
		// TODO dla dowolnego ID (którego nie ma w bazie) listuje wszystkie kolekcje
		return collectionService.get(Integer.parseInt(id));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody CollectionDTO updateCollection(@PathVariable String id, @RequestBody CollectionDTO obj) {
		// TODO czasem nie wyłapuje metody - trzeba by weryfikować, czy istnieje obiekt o podanym ID
		obj.setId(id);
		collectionService.update(obj);
		return obj;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeCollection(@PathVariable String id) {
		collectionService.remove(Integer.parseInt(id));
	}

//	@ExceptionHandler(value = {NumberFormatException.class, IndexOutOfBoundsException.class, StaleStateException.class})
	@ExceptionHandler(value = Exception.class)	// łapie wszystkie wyjątki
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String exceptionHandler() {
		// TODO
		return "";
	}
}
