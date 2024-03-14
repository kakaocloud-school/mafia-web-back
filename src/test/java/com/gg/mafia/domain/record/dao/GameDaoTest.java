package com.gg.mafia.domain.record.dao;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.record.domain.Game;
import com.gg.mafia.domain.record.domain.GameParticipation;
import com.gg.mafia.domain.record.dto.GameSearchRequest;
import com.gg.mafia.global.common.request.SearchFilter;
import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.auditing.AuditingConfig;
import com.gg.mafia.global.config.db.TestDbConfig;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class, AuditingConfig.class})
@DisplayName("게임 DAO 테스트")
@Transactional
@Slf4j
public class GameDaoTest {
    @Autowired
    GameDao gameDao;

    private final Random rand;

    public GameDaoTest() {
        this.rand = new Random();
    }

    @BeforeEach
    public void beforeEach() {

    }

    @AfterEach
    public void afterEach() {

    }

    @Test
    @DisplayName("게임 기록 생성 테스트")
    public void testSaveGame() {
        // Given
        Game game = saveGame();

        // When
        Game gameFromDb = gameDao.findById(game.getId()).orElse(null);

        // Then
        Assertions.assertThat(gameFromDb).isEqualTo(game);
    }

    @Test
    @DisplayName("게임 기록 검색 테스트 - 마피아 측 승리 게임 검색")
    public void testSearchMafiaWonGame() {
        // Given
        int gameNumber = 10;
        List<Game> games = saveGames(gameNumber);
        int mafiaWonCount = (int) games.stream().filter(Game::getMafiaWon).count();

        // When
        GameSearchRequest request = GameSearchRequest.builder()
                .mafiaWon(true)
                .build();
        SearchFilter filter = SearchFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Game> mafiaWonGames = gameDao.search(request, filter, pageable);
        int mafiaWonCountFromDb = (int) mafiaWonGames.stream().filter(Game::getMafiaWon).count();

        // Then
        Assertions.assertThat(mafiaWonCountFromDb).isEqualTo(mafiaWonCount);
    }

    @Test
    @DisplayName("게임 기록 검색 테스트 - 3라운드 이상 진행된 게임 검색")
    public void testSearchGameGte3Rounds() {
        // Given
        int gameNumber = 10;
        List<Game> games = saveGames(gameNumber);
        Predicate<Game> condition = game -> game.getRound() >= 3;
        int matchCount = (int) games.stream().filter(condition).count();

        // When
        GameSearchRequest request = GameSearchRequest.builder()
                .roundGte(3)
                .build();
        SearchFilter filter = SearchFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Game> mafiaWonGames = gameDao.search(request, filter, pageable);
        int matchCountFromDb = (int) mafiaWonGames.stream().filter(condition).count();

        // Then
        Assertions.assertThat(matchCountFromDb).isEqualTo(matchCount);
    }

    @Test
    @DisplayName("게임 기록 검색 테스트 - 특정 유저 참여한 게임 검색")
    public void testSearchGameByPlayer() {
        // Given
        int gameNumber = 10;
        int playedCount = rand.nextInt(1, gameNumber);
        List<Game> playedGames = createGames(playedCount);
        List<Game> unplayedGames = createGames(gameNumber - playedCount);
        User user = createUser(0);
        createGameParticipations(user, playedGames);
        gameDao.saveAll(playedGames);
        gameDao.saveAll(unplayedGames);

        // When
        GameSearchRequest request = GameSearchRequest.builder()
                .userId(user.getId())
                .build();
        SearchFilter filter = SearchFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        List<Game> playedGamesFromDb = gameDao.search(request, filter, pageable)
                .get()
                .toList();

        // Then
        Assertions.assertThat(playedGamesFromDb).isEqualTo(playedGames);
    }

    private List<Game> saveGames(int number) {
        List<Game> games = createGames(number);
        gameDao.saveAll(games);
        return games;
    }

    private Game saveGame() {
        Game game = createGame();
        return gameDao.save(game);
    }

    private List<Game> createGames(int number) {
        return Stream.generate(this::createGame)
                .limit(number)
                .toList();
    }

    private List<GameParticipation> createGameParticipations(User user, List<Game> games) {
        return games.stream()
                .map(game -> createGameParticipation(user, game))
                .toList();
    }

    private Game createGame() {
        return Game.builder()
                .round(rand.nextInt(1, 10))
                .mafiaWon(rand.nextBoolean())
                .build();
    }

    private User createUser(int idx) {
        return User.builder()
                .email("user%d@gmail.com".formatted(idx))
                .password("test123")
                .build();
    }

    private GameParticipation createGameParticipation(User user, Game game) {
        return GameParticipation.builder()
                .user(user)
                .game(game)
                .build();
    }
}
