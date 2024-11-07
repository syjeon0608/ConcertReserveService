package com.hhpl.concertreserve.infra.redis.waitingQueue;

import com.hhpl.concertreserve.domain.error.CoreException;
import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueCacheRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.hhpl.concertreserve.domain.error.ErrorType.QUEUE_IS_INACTIVE;
import static com.hhpl.concertreserve.domain.error.ErrorType.QUEUE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class WaitingQueueCacheRepositoryImpl implements WaitingQueueCacheRepository {

    private final RedissonClient redissonClient;

    private static final String WAITING_QUEUE_KEY = "waiting_queue";
    private static final String ACTIVE_TOKEN_PREFIX = "active_token:";

    @Override
    public void addToWaitingQueue(String token) {
        RScoredSortedSet<String> waitingQueue = redissonClient.getScoredSortedSet(WAITING_QUEUE_KEY);
        long timestamp = System.currentTimeMillis();
        waitingQueue.add(timestamp, token);
    }

    @Override
    public Integer getQueuePosition(String token) {
        RScoredSortedSet<String> waitingQueue = redissonClient.getScoredSortedSet(WAITING_QUEUE_KEY);
        Integer rank = waitingQueue.rank(token);
        if (rank == null) {
            throw new CoreException(QUEUE_NOT_FOUND);
        }
        return rank;
    }

    public void activateTokens(int count, long ttlMillis) {
        RScoredSortedSet<String> waitingQueue = redissonClient.getScoredSortedSet(WAITING_QUEUE_KEY);
        Set<String> tokens = new HashSet<>(waitingQueue.pollFirst(count));

        tokens.forEach(token -> {
            RBucket<String> tokenBucket = redissonClient.getBucket(ACTIVE_TOKEN_PREFIX + token);
            tokenBucket.set("", ttlMillis, TimeUnit.MILLISECONDS);
        });
    }

    @Override
    public void expireTokenForPayment(String token) {
        RBucket<String> tokenBucket = redissonClient.getBucket(ACTIVE_TOKEN_PREFIX + token);
        tokenBucket.delete();
    }

    @Override
    public boolean isValid(String token) {
        RBucket<String> tokenBucket = redissonClient.getBucket(ACTIVE_TOKEN_PREFIX + token);
        if(!tokenBucket.isExists()){
            throw new CoreException(QUEUE_IS_INACTIVE);
        }
        return tokenBucket.isExists();
    }

}
