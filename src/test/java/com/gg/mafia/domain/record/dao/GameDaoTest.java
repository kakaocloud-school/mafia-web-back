package com.gg.mafia.domain.record.dao;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.record.domain.Game;
import com.gg.mafia.domain.record.domain.GameParticipation;
import com.gg.mafia.domain.record.domain.GameRound;
import com.gg.mafia.domain.record.domain.JobEnum;
import com.gg.mafia.domain.record.dto.ActionSuccessCountDto;
import com.gg.mafia.domain.record.dto.GameSearchRequest;
import com.gg.mafia.global.common.request.SearchQuery;
import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.auditing.AuditingConfig;
import com.gg.mafia.global.config.db.TestDbConfig;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.IntStream;
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
    private final Random rand;
    @Autowired
    GameDao gameDao;

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
        SearchQuery searchQuery = SearchQuery.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Game> mafiaWonGames = gameDao.search(request, searchQuery, pageable);
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
        SearchQuery searchQuery = SearchQuery.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Game> mafiaWonGames = gameDao.search(request, searchQuery, pageable);
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
        SearchQuery searchQuery = SearchQuery.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        List<Game> playedGamesFromDb = gameDao.search(request, searchQuery, pageable)
                .get()
                .toList();

        // Then
        Assertions.assertThat(playedGamesFromDb).isEqualTo(playedGames);
    }

    @Test
    @DisplayName("게임 기록 조회 테스트 - 마피아 직업으로 참여한 Game 데이터의 killSuccessCount의 합")
    public void testGetKillSuccessCountPlayingMafia() {
        // Given
        int gameNumber = 10;
        List<Game> games = createGames(gameNumber);
        User user = createUser(0);
        List<GameParticipation> gameParticipations = createGameParticipations(user, games);
        Integer playerActionCount = gameParticipations.stream()
                .filter(gameParticipation -> gameParticipation.getJob().equals(JobEnum.MAFIA))
                .mapToInt(gameParticipation -> gameParticipation.getGame().getKillSuccessCount())
                .sum();
        gameDao.saveAll(games);

        // When
        ActionSuccessCountDto dto = ActionSuccessCountDto.builder()
                .userId(user.getId())
                .actionBy(JobEnum.MAFIA)
                .job(JobEnum.MAFIA)
                .build();
        Integer playerActionCountFromDb = gameDao.getActionSuccessCount(dto);

        // Then
        Assertions.assertThat(playerActionCountFromDb).isEqualTo(playerActionCount);
    }

    @Test
    @DisplayName("게임 기록 조회 테스트 - 마피아 직업으로 참여하여 투표로 죽은 게임 횟수")
    public void testGetVotedGamesPlayingMafia() {
        // Given
        int gameNumber = 20;
        List<Game> games = createGames(gameNumber);
        User user = createUser(0);
        List<GameParticipation> gameParticipations = createGameParticipations(user, games);
        games.forEach(game -> createGameRounds(game, List.of(user), rand.nextInt(10)));
        Long votedGames = gameParticipations.stream()
                .filter(gameParticipation -> gameParticipation.getJob().equals(JobEnum.MAFIA))
                .filter(gameParticipation -> {
                    List<GameRound> grs = gameParticipation.getGame().getGameRounds();
                    return grs.stream()
                            .map(gameRound -> gameRound.getVotedPlayer() != null && gameRound.getVotedPlayer()
                                    .equals(user))
                            .reduce(false, (e1, e2) -> e1 || e2);
                })
                .count();
        gameDao.saveAll(games);

        // When
        GameSearchRequest request = GameSearchRequest.builder()
                .userId(user.getId())
                .job(JobEnum.MAFIA)
                .votedPlayerId(user.getId())
                .build();
        Long votedGamesFromDb = gameDao.searchForCount(request, SearchQuery.builder().build());

        // Then
        Assertions.assertThat(votedGamesFromDb).isEqualTo(votedGames);
    }

    @Test
    @DisplayName("게임 기록 조회 테스트 - 마피아 직업으로 참여하여 생존한 게임 횟수")
    public void testSurvivedGamesPlayingMafia() {
        // Given
        int gameNumber = 20;
        List<Game> games = createGames(gameNumber);
        User user = createUser(0);
        List<GameParticipation> gameParticipations = createGameParticipations(user, games);
        Long survivedGames = gameParticipations.stream()
                .filter(gameParticipation -> gameParticipation.getJob().equals(JobEnum.MAFIA))
                .filter(GameParticipation::getSurvival)
                .count();
        gameDao.saveAll(games);

        // When
        GameSearchRequest request = GameSearchRequest.builder()
                .userId(user.getId())
                .job(JobEnum.MAFIA)
                .survival(true)
                .build();
        Long survivedGamesFromDb = gameDao.searchForCount(request, SearchQuery.builder().build());

        // Then
        Assertions.assertThat(survivedGamesFromDb).isEqualTo(survivedGames);
    }

    @Test
    @DisplayName("게임 기록 조회 테스트 - 마피아 직업으로 참여하여 경찰에게 걸린 적이 있는 게임 횟수")
    public void testGetDetectedGamesPlayingMafia() {
        // Given
        int gameNumber = 20;
        List<Game> games = createGames(gameNumber);
        User user = createUser(0);
        List<GameParticipation> gameParticipations = createGameParticipations(user, games);
        games.forEach(game -> createGameRounds(game, List.of(user), rand.nextInt(10)));
        Long detectedGames = gameParticipations.stream()
                .filter(gameParticipation -> gameParticipation.getJob().equals(JobEnum.MAFIA))
                .filter(gameParticipation -> {
                    List<GameRound> grs = gameParticipation.getGame().getGameRounds();
                    return grs.stream()
                            .map(gameRound -> gameRound.getDetectedPlayer() != null && gameRound.getDetectedPlayer()
                                    .equals(user))
                            .reduce(false, (e1, e2) -> e1 || e2);
                })
                .count();
        gameDao.saveAll(games);

        // When
        GameSearchRequest request = GameSearchRequest.builder()
                .userId(user.getId())
                .job(JobEnum.MAFIA)
                .detectedPlayerId(user.getId())
                .build();
        Long detectedGamesFromDb = gameDao.searchForCount(request, SearchQuery.builder().build());

        // Then
        Assertions.assertThat(detectedGamesFromDb).isEqualTo(detectedGames);
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
        Game game = Game.builder()
                .round(rand.nextInt(1, 10))
                .mafiaWon(rand.nextBoolean())
                .build();
        game.setKillSuccessCount(rand.nextInt(5));
        game.setCureSuccessCount(rand.nextInt(5));
        game.setDetectSuccessCount(rand.nextInt(5));
        return game;
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
                .job(getRandomJob())
                .survival(rand.nextBoolean())
                .build();
    }

    private List<GameRound> createGameRounds(Game game, List<User> users, int lastRound) {
        return IntStream.range(1, lastRound)
                .mapToObj((round) -> createGameRound(game, users, round))
                .toList();
    }

    private GameRound createGameRound(Game game, List<User> users, int round) {
        return GameRound.builder()
                .game(game)
                .round(round)
                .votedPlayer(getRandomUserOrNull(users))
                .killedPlayer(getRandomUserOrNull(users))
                .curedPlayer(getRandomUserOrNull(users))
                .detectedPlayer(getRandomUserOrNull(users))
                .build();
    }

    private User getRandomUserOrNull(List<User> users) {
        if (rand.nextBoolean()) {
            return null;
        }
        return getRandomUser(users);
    }

    private User getRandomUser(List<User> users) {
        int userIndex = rand.nextInt(users.size());
        return users.get(userIndex);
    }

    private JobEnum getRandomJob() {
        int jobIndex = rand.nextInt(JobEnum.values().length);
        return JobEnum.getByValue(jobIndex);
    }
}
