package com.hhpl.concertreserve.app.waitingqueue.domain.service;

import com.hhpl.concertreserve.app.waitingqueue.domain.WaitingQueue;
import com.hhpl.concertreserve.app.waitingqueue.domain.WaitingQueueValidator;
import com.hhpl.concertreserve.app.waitingqueue.domain.repository.WaitingQueueCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static org.hibernate.query.sqm.tree.SqmNode.log;


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

    public WaitingQueue getQueuePosition(String token) {
        Integer queuePosition = queueRepository.getQueuePosition(token);
        return new WaitingQueue(token, queuePosition);
    }

    public void activateTokens(int count, int expireTime) {
        queueRepository.activateTokens(count,expireTime);
    }

    public void expireTokenAfterPayment(String token){
        queueRepository.expireTokenForPayment(token);
        log.info("TOKEN EXPIRED: " + token);
    }

}
