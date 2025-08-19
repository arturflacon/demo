package com.leilao.backend.validator;

import com.leilao.backend.model.Leilao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidadorPeriodoLeilao implements ConstraintValidator<ValidarPeriodoLeilao, Leilao> {

    @Override
    public boolean isValid(Leilao leilao, ConstraintValidatorContext context) {
        if (leilao == null || leilao.getDataHoraInicio() == null || leilao.getDataHoraFim() == null) {
            return true; // Deixa outras validações tratarem nulls
        }

        return leilao.getDataHoraInicio().isBefore(leilao.getDataHoraFim());
    }
}
