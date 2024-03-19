package com.gg.mafia.domain.member.application;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class ThrottlingService {
     private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public boolean allowRequest(String ipAddress) {
        Bucket bucket = buckets.computeIfAbsent(ipAddress, k -> createBucket());

        return bucket.tryConsume(1);
    }

    private Bucket createBucket() {
        Bandwidth limit = Bandwidth.classic(30, Refill.intervally(10, Duration.ofSeconds(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }
}
