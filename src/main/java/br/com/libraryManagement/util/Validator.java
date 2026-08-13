package br.com.libraryManagement.util;

import br.com.libraryManagement.model.exceptions.StatusChangeException;
import br.com.libraryManagement.model.exceptions.ValidatorException;

public class Validator {

    public static void requireNotNull(Object object, String fieldName) {
        if (object == null) {
            throw new ValidatorException(fieldName + " não pode ser nulo.");
        }
    }

    public static void requireNotBlank(String value, String fieldName) {
        requireNotNull(value, fieldName);
        if (value.isBlank()) {
            throw new ValidatorException(fieldName + " não pode ser vazio.");
        }
    }

    public static void requireDescriptionLength(String value, String fieldName) {
        requireNotBlank(value, fieldName);
        int min = 10;
        int max = 100;
        if (value.length() < min || value.length() > max) {
            throw new ValidatorException(fieldName + " deve conter entre: " + min + " e " + max + " caracteres.");
        }
    }

    public static void requireNameLength(String value, String fieldName) {
        requireNotBlank(value, fieldName);
        int min = 3;
        int max = 50;
        if (value.length() < min || value.length() > max) {
            throw new ValidatorException(fieldName + " deve conter entre: " + min + " e " + max + " caracteres.");
        }
    }

    public static void requireTitleLength(String value, String fieldName) {
        requireNotBlank(value, fieldName);
        int min = 5;
        int max = 100;
        if (value.length() < min || value.length() > max) {
            throw new ValidatorException(fieldName + " deve conter entre: " + min + " e " + max + " caracteres.");
        }
    }

    public static void requirePublicationYearValid(Integer value, String fieldName) {
        requireNotNull(value, fieldName);
        int minimumYear = 1450;
        int maximumYear = 2026;
        if (value < minimumYear || value > maximumYear) {
            throw new ValidatorException(fieldName + " deve estar entre os anos de: "
                    + minimumYear + " e "
                    + maximumYear + ".");
        }
    }

}

