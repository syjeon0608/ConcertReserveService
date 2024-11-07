package com.hhpl.concertreserve.domain.waitingqueue;

import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueuePojo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WaitingQueueCacheService {

    private final WaitingQueueCacheRepository queueRepository;
    private final WaitingQueueValidator waitingQueueValidate;

    public void validateUuid(String uuid){
        waitingQueueValidate.validateUserUuid(uuid);
    }

    public boolean checkActiveTokenValidity(String uuid) {
        return queueRepository.isValid(uuid);
    }

    public void enterWaitingQueue(String token) {
        queueRepository.addToWaitingQueue(token);
    }

    public WaitingQueuePojo getQueuePosition(String token) {
        Integer queuePosition = queueRepository.getQueuePosition(token);
        return new WaitingQueuePojo(token, queuePosition);
    }

    public void activateTokens(int count, int expireTime) {
        queueRepository.activateTokens(count,expireTime);
    }

    public void expireTokenAfterPayment(String token){
        queueRepository.expireTokenForPayment(token);
    }

}
