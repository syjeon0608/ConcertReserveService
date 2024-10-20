package com.hhpl.concertreserve.interfaces.api.mapper;

import com.hhpl.concertreserve.application.model.WaitingQueueInfo;
import com.hhpl.concertreserve.interfaces.dto.waitingqueue.WaitingQueueResponse;
import org.springframework.stereotype.Component;

@Component
public class WaitingQueueMapperController {

    public WaitingQueueResponse toResponse(WaitingQueueInfo waitingQueueinfo) {
        return new WaitingQueueResponse(
                waitingQueueinfo.concertId(),
                waitingQueueinfo.queueStatus().name(),
                waitingQueueinfo.renamingQueueNo()
        );
    }

}
