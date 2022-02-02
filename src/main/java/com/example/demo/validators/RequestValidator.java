package com.example.demo.validators;

import com.example.demo.models.Requests;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Requests.class.equals(aClass);
    }

    @SneakyThrows
    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateStart", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateStartMax", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateStop", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateStopMax", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "typeGoods", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "money", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "weight", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "volume", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "destination_lat", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "destination_lng", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "source_lat", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "source_lng", "NotEmpty");
    }
}
