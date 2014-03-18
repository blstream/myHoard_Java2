package com.blstream.myhoard.validator;

import com.blstream.myhoard.biz.exception.MyHoardException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringValidator implements ConstraintValidator<CheckString, String> {

    private ValidationOpt opt;

    @Override
    public void initialize(CheckString a) {
        opt = a.value();
        if (opt.equals(ValidationOpt.NO_ACTION))
            throw new MyHoardException(1, "Wybrano opcję NO_ACTION w walidatorze");
    }

    @Override
    public boolean isValid(String t, ConstraintValidatorContext cvc) {
        if (t == null)  // adnotacja @NotNull powinna to wyłapać
            return true;
        switch (opt) {
            case ITEM_NAME:
                return t.length() < 2 || (t.length() == 2 && t.trim().length() < 2);
            case COLLECTION_NAME:
                return "".equals(t) || "".equals(t.trim());
            default: return true;
        }
    }

}
