package ru.samokhin.labCheck.staging.validationUtils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Component
public class EmailValidationUtil {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@([A-Za-z0-9.-]+)\\.([A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);

    public boolean isValidEmail(String email) {
        if (!StringUtils.hasLength(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
