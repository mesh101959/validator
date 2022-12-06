package com.models.service;


import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class RequestTypeValidator {
    public static final String EMAIL_REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static final String AUTH_TOKEN = "Bearer .*";

    public boolean validateType(String[] types, Object value) {
        for (String type : types) {
            switch(type){
                case "Int":
                    if(value instanceof Integer) return true;
                    break;
                case "String":
                    if(value instanceof String) return true;
                    break;
                case "Boolean":
                    if(value instanceof Boolean) return true;
                    break;
                case "List":
                    if(value instanceof Collection) return true;
                    break;
                case "Date":
                    if(value instanceof String && isDate((String) value)) return true;
                    break;
                case "Email":
                    if(value instanceof String && isEmail((String) value)) return true;
                    break;
                case "UUID":
                    if(value instanceof String && isUUID((String) value)) return true;
                    break;
                case "Auth-Token":
                    if(value instanceof String && isAuthToken((String) value)) return true;
                    break;
            }
        }
        return false;
    }

    private boolean isAuthToken(String value) {
        return patternMatches(value, AUTH_TOKEN);
    }

    private boolean isUUID(String value) {
        try {
            UUID uuid = UUID.fromString(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isEmail(String value) {
        return patternMatches(value, EMAIL_REGEX_PATTERN);
    }

    boolean patternMatches(String string, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(string)
                .matches();
    }

    private boolean isDate(String value) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date dateStr = formatter.parse(value);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
