package com.blstream.myhoard.validator;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringValidator implements ConstraintValidator<CheckString, String> {

    private ValidationOpt opt;

    @Override
    public void initialize(CheckString a) {
        opt = a.value();
        if (opt.equals(ValidationOpt.NO_ACTION))
            throw new MyHoardException(ErrorCode.INTERNAL_SERVER_ERROR).add("validator", "Selected action 'NO_ACTION'");
    }

    @Override
    public boolean isValid(String t, ConstraintValidatorContext cvc) {
        if (t == null)  // adnotacja @NotNull powinna to wyłapać
            return true;
        switch (opt) {
            case ITEM_NAME:
                return !(t.length() < 2 || t.length() > 100 || (t.length() == 2 && t.trim().length() < 2));
            case COLLECTION_NAME:
                return !(t.isEmpty() || t.trim().length() < 2 || t.charAt(0) == ' ' || t.charAt(t.length() - 1) == ' ');
            default: return true;
        }
    }

}
