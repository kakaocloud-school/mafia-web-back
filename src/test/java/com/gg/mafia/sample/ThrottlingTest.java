package com.gg.mafia.sample;

import com.gg.mafia.domain.member.application.ThrottlingService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThrottlingTest {
    private ThrottlingService throttlingService = new ThrottlingService();

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

                    boolean allowed = throttlingService.allowRequest(clientIP);
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

                    boolean allowed = throttlingService.allowRequest(clientIP);
                    log.debug("요청IP : " + clientIP + " 전송 여부 :  " + allowed);

                    try {
                        Thread.sleep(1000);
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
        }
    }
}
