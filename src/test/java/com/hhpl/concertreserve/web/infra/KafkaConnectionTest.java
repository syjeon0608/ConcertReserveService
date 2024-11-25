package com.hhpl.concertreserve.web.infra;

import com.hhpl.concertreserve.app.payment.domain.event.TestProducer;
import com.hhpl.concertreserve.app.payment.interfaces.event.TestConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class KafkaConnectionTest {

    @Autowired
    private TestProducer testProducer;

    @Autowired
    private TestConsumer testConsumer;

    @Test
    void testKafkaMessageSendAndReceive() throws InterruptedException {
        testProducer.sendMessage("test-topic", "Hello Kafka!!!");
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(3, TimeUnit.SECONDS);

        assertThat(testConsumer.getReceivedMessage()).isEqualTo("Hello Kafka!!!");
    }
}