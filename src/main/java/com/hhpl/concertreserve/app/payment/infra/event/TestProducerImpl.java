package com.hhpl.concertreserve.app.payment.infra.event;

import com.hhpl.concertreserve.app.payment.domain.event.TestProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestProducerImpl implements TestProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        log.info("Message sent: {}", message);
    }

}