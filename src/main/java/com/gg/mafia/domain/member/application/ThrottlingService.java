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
    private static final int MAX_TOTAL_BUCKETS = 100; // 최대 버킷 수
    private static final int DELETE_BUCKETS = 10; // 삭제할 버킷 개수

    private static final int MAX_IP_BUCKETS = 10; // ip당 최대 버킷 수
     private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public boolean allowRequest(String ipAddress) {
        Bucket bucket = buckets.computeIfAbsent(ipAddress, k -> createBucket());

        if (buckets.size() >= MAX_TOTAL_BUCKETS) {
            //삭제함수 구현
            removeOldestBucket(DELETE_BUCKETS);
        }

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

    public int getDeleteBuckets() {
        return DELETE_BUCKETS;
    }
    public int getMAxIpBuckets() {
        return MAX_IP_BUCKETS;
    }

    private void removeOldestBucket(int deleteCount) {
        for (int i = 0; i < deleteCount; i++) {
            String oldestBucketKey = null;
            long maxTokens = Long.MIN_VALUE;

            // 가장 많은 토큰을 가진 버킷 찾기
            for (Map.Entry<String, Bucket> entry : buckets.entrySet()) {
                Bucket bucket = entry.getValue();
                long availableTokens = bucket.getAvailableTokens();
                if (availableTokens > maxTokens) {
                    maxTokens = availableTokens;
                    oldestBucketKey = entry.getKey();
                }
            }

            // 가장 많은 토큰을 가진 버킷 삭제
            if (oldestBucketKey != null) {
                buckets.remove(oldestBucketKey);
            } else {
                break;
            }
        }


    }



}
