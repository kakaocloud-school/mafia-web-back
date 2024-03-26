package com.gg.mafia.infra.throttling;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import java.time.Duration;
import java.util.Map;

public class ThrottlingManager {
    private static final int MAX_TOTAL_BUCKETS = 100; // 최대 버킷 수

    private static final int MAX_IP_BUCKETS = 10; // ip당 최대 버킷 수
     private final Map<String, Bucket> buckets = new ConcurrentLinkedHashMap.Builder<String, Bucket>()
             .maximumWeightedCapacity(MAX_TOTAL_BUCKETS)
             .build();

    public boolean allowRequest(String ipAddress) {
        Bucket bucket = buckets.computeIfAbsent(ipAddress, k -> createBucket());

        return bucket.tryConsume(1);
    }

    private Bucket createBucket() {
        Bandwidth limit = Bandwidth.classic(MAX_IP_BUCKETS, Refill.intervally(1, Duration.ofSeconds(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }

    public Map<String, Bucket> getBuckets() {
        return buckets;
    }

    public int getMaxTotalBuckets() {
        return MAX_TOTAL_BUCKETS;
    }


    public int getMAxIpBuckets() {
        return MAX_IP_BUCKETS;
    }




}
