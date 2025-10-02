package com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PathVariableNullableException extends RuntimeException {
    public PathVariableNullableException(String message) {
        super(message);
    }
}
