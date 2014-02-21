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

@Controller 
@RequestMapping("/collections") 
public class CollectionController {

    @Autowired
	CollectionService collectionService;
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<CollectionDTO> getCollections() {
		return collectionService.getList();
	}
	
	@RequestMapping(value="{id}", method = RequestMethod.GET)
	public @ResponseBody CollectionDTO getCollection(@PathVariable String id) {
		Integer idInteger = Integer.parseInt(id);
		return collectionService.get(idInteger);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED) 	
	public void addCollection(@RequestBody CollectionDTO obj) {
		collectionService.create(obj);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public void updateCollection(@PathVariable String id, @RequestBody CollectionDTO obj) {
		Integer idInteger = Integer.parseInt(id);
		collectionService.update(obj);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public void updateCollection(@PathVariable String id) {
		Integer idInteger = Integer.parseInt(id);
		collectionService.remove(idInteger);
	}	
}
