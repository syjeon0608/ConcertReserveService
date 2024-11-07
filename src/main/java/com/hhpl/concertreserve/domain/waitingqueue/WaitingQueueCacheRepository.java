package com.hhpl.concertreserve.domain.waitingqueue;

public interface WaitingQueueCacheRepository {
    void addToWaitingQueue(String token);
    Integer getQueuePosition(String token);
    void expireTokenForPayment(String token);
    void activateTokens(int count, long ttlMillis);
}
