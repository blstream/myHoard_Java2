package com.blstream.myhoard.validator;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringValidator.class)
@Documented
public @interface CheckString {

    String message() default "Niepoprawny String";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    ValidationOpt value() default ValidationOpt.NO_ACTION;
}
