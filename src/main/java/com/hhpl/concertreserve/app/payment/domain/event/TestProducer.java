package com.hhpl.concertreserve.app.payment.domain.event;

public interface TestProducer {
    void sendMessage(String topic, String message);
}