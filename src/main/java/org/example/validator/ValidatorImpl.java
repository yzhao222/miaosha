package org.example.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {
    private Validator validator;
    @Override
    public void afterPropertiesSet() throws Exception {
        this.validator= Validation.buildDefaultValidatorFactory().getValidator();
    }
    public  ValidationResult validate(Object bean){
        ValidationResult validationResult=new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet=validator.validate(bean);
        if(constraintViolationSet.size()>0){
            validationResult.setHasErrors(true);
            constraintViolationSet.forEach(constraintViolation->{
                String errorMessage=constraintViolation.getMessage();
                String propertyName=constraintViolation.getPropertyPath().toString();
                validationResult.getErrorMessageMap().put(propertyName,errorMessage);
            });
        }
        return validationResult;
    }
}
