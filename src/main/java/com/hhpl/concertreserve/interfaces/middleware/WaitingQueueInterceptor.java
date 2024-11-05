package com.hhpl.concertreserve.interfaces.middleware;

import com.hhpl.concertreserve.application.facade.WaitingQueueFacade;
import com.hhpl.concertreserve.interfaces.middleware.util.ConcertIdExtractor;
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
        String requestURI = request.getRequestURI();
        Long concertId = ConcertIdExtractor.extractConcertId(requestURI);

        return waitingQueueFacade.validateQueueReadinessForSubsequent(uuid, concertId);
    }

}
