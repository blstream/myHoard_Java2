package com.blstream.myhoard.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.blstream.myhoard.biz.model.*;

public class CollectionDTOValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CollectionDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "collectionDTO.name.empty");
    }

}
