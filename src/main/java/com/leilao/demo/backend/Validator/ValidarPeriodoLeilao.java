package com.leilao.backend.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ValidadorPeriodoLeilao.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidarPeriodoLeilao {

    String message() default "Data de início deve ser anterior à data de fim";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
