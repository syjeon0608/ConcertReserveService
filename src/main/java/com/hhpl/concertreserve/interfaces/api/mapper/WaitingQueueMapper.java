package com.hhpl.concertreserve.interfaces.api.mapper;

import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueueInfo;
import com.hhpl.concertreserve.interfaces.dto.waitingqueue.WaitingQueueCreatedResponse;
import com.hhpl.concertreserve.interfaces.dto.waitingqueue.WaitingQueueStatusResponse;
import org.springframework.stereotype.Component;

@Component
public class WaitingQueueMapper {

    public WaitingQueueStatusResponse toWaitingQueueStatusResponse(WaitingQueueInfo info) {
        return new WaitingQueueStatusResponse(
                info.concertId(),
                info.queueStatus(),
                info.renamingQueueNo()
        );
    }

    public WaitingQueueCreatedResponse toWaitingQueueCreatedResponse(WaitingQueue waitingQueue) {
        return new WaitingQueueCreatedResponse(
                waitingQueue.getUuid(),
                waitingQueue.getConcertId(),
                waitingQueue.getCreatedAt()
        );
    }
}
