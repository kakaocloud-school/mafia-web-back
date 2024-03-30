package com.gg.mafia.domain.record;

import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.QUser;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.record.dao.GameDao;
import com.gg.mafia.domain.record.domain.Game;
import com.gg.mafia.domain.record.domain.GameParticipation;
import com.gg.mafia.domain.record.domain.GameRound;
import com.gg.mafia.domain.record.domain.JobEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/root-context.xml")
@Slf4j
public class GameDummyDataTest {
    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    GameDao gameDao;

    @Autowired
    UserDao userDao;

    Random rand;

    public GameDummyDataTest() {
        this.rand = new Random();
    }

    @Disabled
    @Test
    @Transactional
    @Rollback(value = false)
    public void saveGames() {
        Long userId = queryFactory.selectFrom(QUser.user).limit(1).offset(0).fetchOne().getId();
        User user = userDao.findById(userId).get();
        List<Game> games = createGames(10);
        createGameParticipations(user, games);
        games.forEach(game -> createGameRounds(game, List.of(user), rand.nextInt(8)));
        gameDao.saveAll(games);
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