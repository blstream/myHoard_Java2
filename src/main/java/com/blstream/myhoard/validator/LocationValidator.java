package com.blstream.myhoard.validator;

import com.blstream.myhoard.biz.model.Location;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocationValidator implements ConstraintValidator<GeographicLocation, Location> {

    @Override
    public void initialize(GeographicLocation a) {
        //
    }

    @Override
    public boolean isValid(Location t, ConstraintValidatorContext cvc) {
        return t == null || t.getLat() >= -90 && t.getLat() <= 90 && t.getLng() >= -180 && t.getLng() <= 180;
    }
    
}
