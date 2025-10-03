package com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ContentNotAvaliableToTheUserException extends RuntimeException {
    public ContentNotAvaliableToTheUserException(String message) {
        super(message);
    }
}
