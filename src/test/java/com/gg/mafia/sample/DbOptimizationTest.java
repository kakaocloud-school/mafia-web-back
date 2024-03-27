package com.gg.mafia.sample;

import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.profile.dao.ProfileDao;
import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.domain.profile.domain.QProfile;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/root-context.xml")
@Slf4j
public class DbOptimizationTest {
    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    ProfileDao profileDao;

    @Autowired
    UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    Random rand;
    int idxCount = 316800;

    public DbOptimizationTest() {
        this.rand = new Random();
    }

    @Test
    @Disabled
    public void createTestData() {
        for (int i = 0; i < 10000; i++) {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            List<? extends Future<?>> futures = Stream.generate(
                    () -> executorService.submit(() -> saveUsersAndProfiles(10))
            ).limit(20).toList();
            futures.forEach(future -> {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
            log.info("loop: %d".formatted(i));
        }
    }

    @Test
    @Disabled
    public void testCoveringIndex() {
        QProfile profile = QProfile.profile;
        List<Long> ids = queryFactory.select(profile.id)
                .from(profile)
                .orderBy(QProfile.profile.citizenWinningRate.desc())
                .limit(10)
                .offset(10000)
                .fetch();
        JPAQuery<Profile> coveringIndexQuery = queryFactory
                .select(profile)
                .from(profile)
                .where(profile.id.in(ids))
                .orderBy(getOrderByFieldsCondition(profile.id, ids));
        JPAQuery<Profile> noCoveringIndexQuery = queryFactory
                .select(profile)
                .from(profile)
                .orderBy(QProfile.profile.citizenWinningRate.desc())
                .limit(10)
                .offset(10000);

        Long coveringIndexCost = Stream.generate(() -> fetchQuery(coveringIndexQuery))
                .limit(10)
                .reduce(0L, Long::sum);

        Long noCoveringIndexCost = Stream.generate(() -> fetchQuery(noCoveringIndexQuery))
                .limit(10)
                .reduce(0L, Long::sum);
        log.info("[coveringIndexCost] time cost: %d".formatted(coveringIndexCost));
        log.info("[noCoveringIndexCost] time cost: %d".formatted(noCoveringIndexCost));
    }

    private long fetchQuery(JPAQuery<?> query) {
        long start = System.currentTimeMillis();
        query.fetch();
        long end = System.currentTimeMillis();
        return end - start;
    }

    private OrderSpecifier<?> getOrderByFieldsCondition(Path<?> field, List<Long> fields) {
        String template = "FIELD({0}, %s)".formatted(
                fields.stream().map(Object::toString).collect(Collectors.joining(", "))
        );
        return Expressions.stringTemplate(template, field).asc();
    }

    private void saveUsersAndProfiles(int size) {
        userDao.saveAll(createUsersAndProfiles(size));
        log.info("saved");
    }

    private List<User> createUsersAndProfiles(int size) {
        int idxLow = getAndUpdateIdxCount(size);
        List<User> users = IntStream.range(idxLow, idxLow + size)
                .mapToObj(this::createUser)
                .toList();
        users.forEach(this::createProfile);
        return users;
    }

    private User createUser(int idx) {
        String username = "testuser%d".formatted(idx);
        log.debug("idx: %d".formatted(idx));
        return User.builder()
                .email("%s@gmail.com".formatted(username))
                .password(passwordEncoder.encode(username))
                .build();
    }

    private Profile createProfile(User user) {
        Profile profile = Profile.builder()
                .user(user)
                .userName(user.getEmail())
                .build();
        profile.setRating(rand.nextInt(1000));
        profile.setCitizenWinningRate(rand.nextFloat(0, 1));
        profile.setMafiaWinningRate(rand.nextFloat(0, 1));
        profile.setDoctorWinningRate(rand.nextFloat(0, 1));
        profile.setPoliceWinningRate(rand.nextFloat(0, 1));
        profile.setWinningRate(rand.nextFloat(0, 1));
        return profile;
    }

    private synchronized int getAndUpdateIdxCount(int size) {
        int old = idxCount;
        idxCount += size;
        return old;
    }
}