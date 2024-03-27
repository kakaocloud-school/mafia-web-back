package com.gg.mafia.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gg.mafia.infra.ratelimiter.ThrottlingManager;
import io.github.bucket4j.Bucket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThrottlingTest {
    private ThrottlingManager throttlingManager = new ThrottlingManager();


    @Test
    public void testMaxConcurrentRequests() throws InterruptedException {

        // 다수의 클라이언트 IP 주소를 시뮬레이트
        String[] clientIPs = {"192.168.1.1", "192.168.1.2", "192.168.1.3", "192.168.1.4", "192.168.1.5"};
        int clients = clientIPs.length;
        int requestCount = 13 ; // 각 스레드가 보낼 요청 수

        ExecutorService executor = Executors.newFixedThreadPool(clients);

        int[] trueCount = new int[clients];
        int[] falseCount = new int[clients];

        for (int i = 0; i < clientIPs.length; i++) {
            final int index = i;
            executor.submit(() -> {
                String clientIP = clientIPs[index];
                for (int j = 0; j < requestCount; j++) {

                    boolean allowed = throttlingManager.allowRequest(clientIP);
                    log.debug("요청IP : " + clientIP + " 전송 여부 :  " + allowed);

                    if (allowed) trueCount[index]++;
                    else falseCount[index]++;
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);


        for (int i = 0; i < clientIPs.length; i++) {
            log.debug("요청IP :" + clientIPs[i] + " 전송 성공 : " + trueCount[i]+"회");
            log.debug("요청IP :" + clientIPs[i] + " 전송 실패 : " + falseCount[i]+"회");

            assertEquals(throttlingManager.getMAxIpBuckets(), trueCount[i]);
        }
    }

    @Test
    public void testMaxConcurrentRequestsWithDelay() throws InterruptedException {

        // 다수의 클라이언트 IP 주소를 시뮬레이트
        String[] clientIPs = {"192.168.1.1", "192.168.1.2", "192.168.1.3", "192.168.1.4", "192.168.1.5"};
        int clients = clientIPs.length;
        int requestCount = 13 ; // 각 스레드가 보낼 요청 수

        ExecutorService executor = Executors.newFixedThreadPool(clients);

        int[] trueCount = new int[clients];
        int[] falseCount = new int[clients];

        for (int i = 0; i < clientIPs.length; i++) {
            final int index = i;
            executor.submit(() -> {
                String clientIP = clientIPs[index];
                for (int j = 0; j < requestCount; j++) {

                    boolean allowed = throttlingManager.allowRequest(clientIP);
                    log.debug("요청IP : " + clientIP + " 전송 여부 :  " + allowed);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if (allowed) trueCount[index]++;
                    else falseCount[index]++;
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);


        for (int i = 0; i < clientIPs.length; i++) {
            log.debug("요청IP :" + clientIPs[i] + " 전송 성공 : " + trueCount[i]+"회");
            log.debug("요청IP :" + clientIPs[i] + " 전송 실패 : " + falseCount[i]+"회");

            assertEquals(requestCount, trueCount[i]);
        }


    }

    @Test
    public void testBucketRemoval() {
        // 버킷 수가 MAX_BUCKETS를 초과하도록 설정
        for (int i = 0; i < throttlingManager.getMaxTotalBuckets()+11; i++) {
            throttlingManager.allowRequest("192.168.1." + i);
            log.debug("버켓사이즈: "+ throttlingManager.getBuckets().size());
        }

        for (Map.Entry<String, Bucket> entry : throttlingManager.getBuckets().entrySet()) {
            String ipAddress = entry.getKey();

            log.debug("버킷에 저장된 IP 주소: " + ipAddress);
        }
        // 버킷이 제대로 삭제되었는지 확인
        assertTrue(throttlingManager.getBuckets().size() <= 100);
    }


}
