package com.hhpl.concertreserve.infra.filter;

import com.hhpl.concertreserve.application.facade.WaitingQueueFacade;
import com.hhpl.concertreserve.domain.error.CoreException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component
@RequiredArgsConstructor
public class WaitingQueueFilter implements Filter {

    private final WaitingQueueFacade waitingQueueFacade;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String uuid = httpRequest.getHeader("X-WAITING-QUEUE-ID");

        if (httpRequest.getRequestURI().matches("/api/v1/users/\\d+/points") || httpRequest.getRequestURI().matches("/api/v1/waiting-queues") ) {
            chain.doFilter(request, response);
            return;
        }

        try{
            waitingQueueFacade.validateWaitingQueueUuid(uuid);
            chain.doFilter(request, response);
        } catch (CoreException e) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentType("application/json; charset=UTF-8");
            httpResponse.setCharacterEncoding("UTF-8");

            String jsonResponse = String.format("{\"error\": \"%s\", \"message\": \"%s\"}", e.getErrorType().name(), e.getErrorType().getMessage());
            httpResponse.getWriter().write(jsonResponse);

            log.info("Error occurred: " + e.getErrorType().name() + " - " +  e.getErrorType().getMessage(), e);

        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}