package com.hhpl.concertreserve.config.web;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class WaitingQueueFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String uuid = httpRequest.getHeader("X-WAITING-QUEUE-ID");

        if (httpRequest.getRequestURI().startsWith("/api/v1/users") && httpRequest.getRequestURI().contains("/points")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException | NullPointerException e) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
            String jsonResponse = "{\"error\": \"INVALID_UUID\", \"message\": \"UUID header is invalid format\"}";
            httpResponse.getWriter().write(jsonResponse);

            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}