package com.hhpl.concertreserve.app.payment.interfaces.event;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Getter
@Slf4j
@Component
public class TestConsumer {

    private String receivedMessage;

    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void consume(String message) {
        this.receivedMessage = message;
        log.info("Received message: {}", message);
    }

}
