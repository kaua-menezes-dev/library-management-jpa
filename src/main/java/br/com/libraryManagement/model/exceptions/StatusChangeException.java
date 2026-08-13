package br.com.libraryManagement.model.exceptions;

public class StatusChangeException extends RuntimeException {
    public StatusChangeException(String message) {
        super(message);
    }
}
