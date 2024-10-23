package com.hhpl.concertreserve.infra.config;

import com.hhpl.concertreserve.infra.filter.WaitingQueueFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final WaitingQueueFilter waitingQueueFilter;

    @Bean
    public FilterRegistrationBean<WaitingQueueFilter> waitingQueueValidationFilter() {
        FilterRegistrationBean<WaitingQueueFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(waitingQueueFilter);
        registrationBean.addUrlPatterns("/api/*");

        return registrationBean;
    }

}