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
@DisplayName("게임 엔티티 테스트")
@Slf4j
public class GameTest {
    private final Random rand;
    @PersistenceUnit
    private EntityManagerFactory emf;
    private EntityManager em;
    private Game game;
    private List<GameParticipation> gameParticipations;
    private List<User> users;

    public GameTest() {
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
    @DisplayName("게임 기록 생성 테스트")
    public void testCreateGame() {
        // Given, 게임 기록 DB row 추가된 상태
        game = Game.builder()
                .build();
        em.persist(game);
        em.flush();
        em.clear();

        // When, DB에서 해당 ID로 조회할 때
        Game gameFromDb = em.find(Game.class, game.getId());

        // Then, 조회한 정보와 저장한 정보가 같은가
        Assertions.assertThat(gameFromDb).isEqualTo(game);
    }

    @Test
    @Order(2)
    @DisplayName("게임 참여 기록 생성 테스트")
    public void testCreateGameParticipation() {
        // Given, 게임 참여 기록 DB row 추가된 상태
        int userNumber = 5;
        game = em.find(Game.class, game.getId());
        users = createUsers(userNumber);
        gameParticipations = createGameParticipations(game, users);
        gameParticipations.forEach(gameParticipation -> em.persist(gameParticipation));
        em.flush();
        em.clear();

        // When, DB에서 해당 ID로 조회할 때
        List<GameParticipation> gameParticipationsFromDb = gameParticipations.stream()
                .map(gameParticipation ->
                        em.find(GameParticipation.class, gameParticipation.getId()))
                .toList();

        // Then, 조회한 정보와 저장한 정보가 같은가
        IntStream.range(0, gameParticipations.size())
                .forEach(idx -> {
                    Assertions.assertThat(gameParticipations.get(idx))
                            .isEqualTo(gameParticipationsFromDb.get(idx));
                });
    }

    @Test
    @Order(3)
    @DisplayName("유저 탈퇴 후 참여 기록 조회 테스트")
    public void testDeleteUser() {
        // Given, 유저가 탈퇴한 상태
        User user = em.find(User.class, users.get(0).getId());
        em.remove(user);
        em.flush();
        em.clear();

        // When, 다시 참여 기록을 조회했을 때
        GameParticipation oldOne = gameParticipations.get(0);
        GameParticipation newOne = em.find(GameParticipation.class, oldOne.getId());
        gameParticipations.set(0, newOne);

        // Then, 참여 기록 조회 가능하고 해당 참여 기록의 유저는 null
        Assertions.assertThat(newOne).isNotNull();
        Assertions.assertThat(newOne.getUser()).isNull();
    }

    @Test
    @Order(4)
    @DisplayName("게임 기록 삭제 후 참여 기록 조회 테스트")
    public void testDeleteGame() {
        // Given, 게임 기록 삭제한 상태
        game = em.find(Game.class, game.getId());
        em.remove(game);
        em.flush();
        em.clear();

        // When, 다시 참여 기록을 조회했을 때
        List<GameParticipation> gameParticipationsFromDb = gameParticipations.stream()
                .map(gameParticipation ->
                        em.find(GameParticipation.class, gameParticipation.getId()))
                .toList();

        // Then, 참여 기록 목록 모두 null
        gameParticipationsFromDb.forEach(gameParticipation ->
                Assertions.assertThat(gameParticipation).isNull());
    }

    private List<User> createUsers(int number) {
        return IntStream.range(0, number)
                .mapToObj(this::createUser)
                .collect(Collectors.toList());
    }

    private User createUser(int idx) {
        return User.builder()
                .email("aaaa%d@gmail.com".formatted(idx))
                .password("test1234")
                .build();
    }

    private List<GameParticipation> createGameParticipations(Game game, List<User> users) {
        return users.stream()
                .map(user -> createGameParticipation(game, user))
                .collect(Collectors.toList());
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
