package com.hhpl.concertreserve.app.waitingqueue.domain.repository;

public interface WaitingQueueCacheRepository {
    void addToWaitingQueue(String token);
    Integer getQueuePosition(String token);
    void expireTokenForPayment(String token);
    void activateTokens(int count, long ttlMillis);
    boolean isValid(String token);
}
