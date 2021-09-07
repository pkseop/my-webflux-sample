package my.spring.sample.webflux.validator;

import my.spring.sample.webflux.enums.Findable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumCheck, Object> {
    private EnumCheck annotation;

    @Override
    public void initialize(EnumCheck constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null)   // do not check null
            return true;

        boolean result = false;
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                Findable findable = (Findable)enumValue;
                if (value.equals(findable.getValue())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
