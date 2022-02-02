package com.example.demo.validators;

import com.example.demo.models.Trucks;
import com.example.demo.services.TrucksService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TruckValidator implements Validator {
//    @Autowired
//    private TrucksService trucksService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Trucks.class.equals(aClass);
    }

    @SneakyThrows
    @Override
    public void validate(Object o, Errors errors) {
//        Trucks truck = (Trucks) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "brand", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emptyprice", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullprice", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "height", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "length", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "width", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "volume", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "weight", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lat", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lng", "NotEmpty");
    }
}
