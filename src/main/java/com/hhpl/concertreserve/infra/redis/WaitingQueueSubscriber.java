package com.hhpl.concertreserve.infra.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingQueueSubscriber {

    private final RedissonClient redissonClient;

    @PostConstruct
    public void subscribeToWaitingQueue() {
        RTopic topic = redissonClient.getTopic("waitingQueueTopic");
        topic.addListener(Long.class, new MessageListener<Long>() {
            @Override
            public void onMessage(CharSequence channel, Long queueNo) {
                processQueueNumber(queueNo);
            }
        });
    }

    private void processQueueNumber(Long queueNo) {
        System.out.println("Received Queue Number: " + queueNo);
    }
}
