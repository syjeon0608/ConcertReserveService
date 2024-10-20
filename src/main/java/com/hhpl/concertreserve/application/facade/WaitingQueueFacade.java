package com.hhpl.concertreserve.application.facade;

import com.hhpl.concertreserve.application.mapper.WaitingQueueMapperApplication;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import com.hhpl.concertreserve.application.model.WaitingQueueInfo;
import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingQueueFacade {

    private final WaitingQueueService waitingQueueService;
    private final WaitingQueueMapperApplication waitingQueueMapper;

    public WaitingQueueInfo enterWaitingQueueForConcert(String uuid, Long concertId){
        waitingQueueService.validateUserBeforeQueueEntry(uuid, concertId);
        WaitingQueue waitingQueue =  waitingQueueService.enterWaitingQueue(uuid, concertId);
        return waitingQueueMapper.from(waitingQueue);
    }

    public WaitingQueueInfo getWaitingQueueInfoForUser(String uuid, Long concertId) {
        WaitingQueue waitingQueue = waitingQueueService.getMyWaitingQueueInfo(uuid, concertId);
        return waitingQueueMapper.from(waitingQueue);
    }

    public void validateQueueStatusForUser(String uuid, Long concertId){
        waitingQueueService.validateQueueStatus(uuid,concertId);
    }

}
