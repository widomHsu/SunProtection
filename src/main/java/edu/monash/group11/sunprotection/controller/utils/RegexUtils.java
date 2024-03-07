package edu.monash.group11.sunprotection.controller.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RegexUtils {

    public boolean checkEmail(String email) {
        String regex = "^[\\w.-]+@([a-z0-9][a-z0-9-]*[a-z0-9]?\\.)+[a-z]{2,}$";
        return Pattern.matches(regex, email);
    }
}
