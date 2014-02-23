package com.blstream.myhoard.biz.model;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CollectionDTOValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return CollectionDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        CollectionDTO collectionDTO = (CollectionDTO) obj;
        
        ValidationUtils.rejectIfEmpty(errors, "name", "collectionDTO.name.empty");
    }

}
