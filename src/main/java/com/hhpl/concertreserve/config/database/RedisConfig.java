package com.hhpl.concertreserve.config.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhpl.concertreserve.app.concert.domain.entity.Concert;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.protocol:redis}")
    private String protocol;

    private final ObjectMapper objectMapper;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setCodec(StringCodec.INSTANCE);
        config.useSingleServer().setAddress(protocol + "://" + host + ":" + port);
        return Redisson.create(config);
    }

    @Bean
    public TypedJsonJacksonCodec concertListCodec() {
        TypeReference<List<Concert>> typeReference = new TypeReference<>() {};
        return new TypedJsonJacksonCodec(typeReference, objectMapper);
    }
}