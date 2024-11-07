package com.hhpl.concertreserve.interfaces.middleware;

import com.hhpl.concertreserve.application.facade.WaitingQueueFacade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class WaitingQueueInterceptor implements HandlerInterceptor {

    private final WaitingQueueFacade waitingQueueFacade;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uuid = request.getHeader("X-WAITING-QUEUE-ID");

        return waitingQueueFacade.validateTokenActivationForNextStep(uuid);
    }

}
