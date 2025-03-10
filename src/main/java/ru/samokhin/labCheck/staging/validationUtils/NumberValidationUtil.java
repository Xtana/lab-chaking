package ru.samokhin.labCheck.staging.validationUtils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Component
public class NumberValidationUtil {
    private static final Pattern STUDENT_CARD_NUMBER_PATTERN =
            Pattern.compile("^\\d+$");

    public boolean isValidNumber(String stringNumber) {
        if (!StringUtils.hasLength(stringNumber)) {
            return false;
        }
        return STUDENT_CARD_NUMBER_PATTERN.matcher(stringNumber).matches();
    }
}
