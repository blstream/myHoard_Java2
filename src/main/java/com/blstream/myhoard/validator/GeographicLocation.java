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
@Constraint(validatedBy = LocationValidator.class)
@Documented
public @interface GeographicLocation {

    String message() default "Niepoprawna lokalizacja";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
