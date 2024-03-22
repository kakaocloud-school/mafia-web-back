package com.gg.mafia.domain.record.domain;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.auditing.AuditingConfig;
import com.gg.mafia.global.config.db.TestDbConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class, AuditingConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("라운드 별 행동 기록 엔티티 테스트")
@Slf4j
public class PlayerActionTest {
    private final Random rand;
    @PersistenceUnit
    private EntityManagerFactory emf;
    private EntityManager em;
    private Game game;
    private GameParticipation gameParticipation;
    private User user;
    private List<PlayerAction> playerActions;

    public PlayerActionTest() {
        this.rand = new Random();
    }

    @BeforeAll
    public void beforeAll() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @AfterAll
    public void afterAll() {
        em.getTransaction().rollback();
        em.clear();
    }

    @AfterEach
    public void afterEach() {
        em.clear();
    }

    @Test
    @Order(1)
    @DisplayName("라운드 별 행동 기록 생성 테스트")
    public void testCreatePlayerAction() {
        // Given, 게임 기록 DB row 추가된 상태
        game = Game.builder()
                .build();
        user = createUser();
        gameParticipation = createGameParticipation(game, user);
        int roundNumber = 5;
        playerActions = createPlayerActions(roundNumber);
        playerActions.forEach(playerAction -> em.persist(playerAction));
        em.flush();
        em.clear();

        // When, DB에서 해당 ID로 조회할 때
        List<PlayerAction> playerActionsFromDb = playerActions.stream().map(
                playerAction -> em.find(PlayerAction.class, playerAction.getId())
        ).toList();

        // Then, 조회한 정보와 저장한 정보가 같은가
        IntStream.range(0, playerActions.size())
                .forEach(idx -> {
                    Assertions.assertThat(playerActions.get(idx))
                            .isEqualTo(playerActionsFromDb.get(idx));
                });
    }

    @Test
    @Order(2)
    @DisplayName("게임 기록 삭제 시 라운드 별 행동 기록 조회 테스트")
    public void testDeleteGame() {
        // Given, 게임 기록 삭제된 상태
        em.remove(em.find(Game.class, game.getId()));
        em.flush();
        em.clear();

        // When, 다시 라운드 별 행동 기록 조회할 때
        List<PlayerAction> playerActionsFromDb = playerActions.stream().map(
                playerAction -> em.find(PlayerAction.class, playerAction.getId())
        ).toList();

        // Then, 라운드 별 행동 기록 모두 null
        IntStream.range(0, playerActionsFromDb.size())
                .forEach(idx -> {
                    Assertions.assertThat(playerActionsFromDb.get(idx))
                            .isNull();
                });
    }

    private List<PlayerAction> createPlayerActions(int number) {
        AtomicBoolean survival = new AtomicBoolean(true);
        return IntStream.range(0, number)
                .mapToObj((idx) -> {
                    survival.set(survival.get() && (rand.nextInt(10) < 8));
                    return createPlayerAction(idx, survival.get());
                })
                .collect(Collectors.toList());
    }

    private PlayerAction createPlayerAction(int idx, boolean survival) {
        return PlayerAction.builder()
                .round(idx + 1)
                .gameParticipation(gameParticipation)
                .survival(survival)
                .build();
    }

    private User createUser() {
        return User.builder()
                .email("aaaa%d@gmail.com")
                .password("test1234")
                .build();
    }

    private GameParticipation createGameParticipation(Game game, User user) {
        int jobIndex = rand.nextInt(JobEnum.values().length);
        JobEnum job = JobEnum.getByValue(jobIndex);
        return GameParticipation.builder()
                .game(game)
                .user(user)
                .job(job)
                .survival(rand.nextBoolean())
                .build();
    }
}
