package com.gabrielrodrigues.encurtador_de_links.exceptions;

import java.util.Date;

public record ExceptionResponse(
        Date timeStamp,
        String Message,
        String details
) {}
