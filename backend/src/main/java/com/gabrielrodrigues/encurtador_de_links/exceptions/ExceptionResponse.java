package com.gabrielrodrigues.encurtador_de_links.exceptions;

import java.time.LocalDateTime;
import java.util.Date;

public record ExceptionResponse(
        LocalDateTime timeStamp,
        String Message,
        String details
) {}
