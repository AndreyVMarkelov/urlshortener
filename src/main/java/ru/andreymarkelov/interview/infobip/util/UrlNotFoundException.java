package ru.andreymarkelov.interview.infobip.util;

public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(String message) {
        super(message);
    }
}
