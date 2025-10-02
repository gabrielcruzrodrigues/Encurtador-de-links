package com.gabrielrodrigues.encurtador_de_links.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/link")
public class LinkController {

    @GetMapping
    public Map<String, Object> ping(HttpServletRequest request) {
        Map<String, Object> response = new LinkedHashMap<>();

        // Informações básicas
        response.put("method", request.getMethod());
        response.put("requestURI", request.getRequestURI());
        response.put("protocol", request.getProtocol());
        response.put("scheme", request.getScheme());
        response.put("serverName", request.getServerName());
        response.put("serverPort", request.getServerPort());

        // Informações do cliente
        response.put("remoteAddr", request.getRemoteAddr());
        response.put("remoteHost", request.getRemoteHost());
        response.put("remotePort", request.getRemotePort());

        // Em caso de proxy/reverso (Traefik, Nginx, etc.)
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null) {
            response.put("realIp", forwardedFor);
        }

        // Query string
        response.put("queryString", request.getQueryString());

        // Headers
        Map<String, String> headers = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            headers.put(header, request.getHeader(header));
        }
        response.put("headers", headers);

        return response;
    }

}
