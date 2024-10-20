package com.hhpl.concertreserve.application.mapper;

import com.hhpl.concertreserve.application.model.WaitingQueueInfo;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import org.springframework.stereotype.Component;

@Component
public class WaitingQueueMapperApplication {

    public WaitingQueueInfo from(WaitingQueue waitingQueue) {
        return new WaitingQueueInfo(
                waitingQueue.getUuid(),
                waitingQueue.getConcertId(),
                waitingQueue.getQueueStatus(),
                waitingQueue.getQueueNo()
        );
    }
}
