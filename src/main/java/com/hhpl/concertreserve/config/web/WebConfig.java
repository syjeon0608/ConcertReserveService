package com.hhpl.concertreserve.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final WaitingQueueInterceptor waitingQueueInterceptor;

    public WebConfig(WaitingQueueInterceptor waitingQueueInterceptor) {
        this.waitingQueueInterceptor = waitingQueueInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(waitingQueueInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/v1/waiting-queues")
                .excludePathPatterns("/api/v1/concerts")
                .excludePathPatterns("/api/v1/users/{userId}/points");
    }
}
