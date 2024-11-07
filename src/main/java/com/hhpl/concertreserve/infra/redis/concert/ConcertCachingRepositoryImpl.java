package com.hhpl.concertreserve.infra.redis.concert;

import com.hhpl.concertreserve.domain.concert.ConcertCachingRepository;
import com.hhpl.concertreserve.domain.concert.model.Concert;
import com.hhpl.concertreserve.infra.database.concert.ConcertRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ConcertCachingRepositoryImpl implements ConcertCachingRepository {

    private final RedissonClient redissonClient;
    private final ConcertRepositoryImpl concertRepository;
    private static final long CACHE_TTL = 24;
    private static final String CONCERT_CACHE_PREFIX = "concert:available:";

    @Override
    public List<Concert> getAvailableConcerts(LocalDateTime now) {
        String cacheKey = CONCERT_CACHE_PREFIX + now.toLocalDate().toString();

        RBucket<List<Concert>> bucket = redissonClient.getBucket(cacheKey);
        List<Concert> concertList = bucket.get();

        if (concertList == null) {
            concertList = concertRepository.getAvailableConcerts(now);
            bucket.set(concertList, CACHE_TTL, TimeUnit.HOURS);
        }
        return concertList;
    }

}