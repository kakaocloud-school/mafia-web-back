package com.gg.mafia.domain.record.dao;

import com.gg.mafia.domain.record.domain.Game;
import com.gg.mafia.domain.record.domain.JobEnum;
import com.gg.mafia.domain.record.domain.QGame;
import com.gg.mafia.domain.record.domain.QGameParticipation;
import com.gg.mafia.domain.record.domain.QGameRound;
import com.gg.mafia.domain.record.dto.GameParticipationSubQueryDto;
import com.gg.mafia.domain.record.dto.GameRoundSubQueryDto;
import com.gg.mafia.domain.record.dto.GameSearchRequest;
import com.gg.mafia.domain.record.dto.ActionSuccessCountDto;
import com.gg.mafia.global.common.request.SearchFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
public class GameDaoImpl implements GameDaoCustom {
    private final JPAQueryFactory queryFactory;

    public Long searchForCount(GameSearchRequest request, SearchFilter filter) {
        QGame game = QGame.game;
        Long count = queryFactory
                .select(game.count())
                .from(game)
                .where(buildSearchCondition(filter), buildRequestCondition(request))
                .fetchOne();
        if (count == null) {
            count = 0L;
        }
        return count;
    }

    public Page<Game> search(GameSearchRequest request, SearchFilter filter, @NonNull Pageable pageable) {
        QGame game = QGame.game;
        List<Game> games = queryFactory
                .selectFrom(game)
                .where(buildSearchCondition(filter), buildRequestCondition(request))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        Long count = searchForCount(request, filter);
        return new PageImpl<>(games, pageable, count);
    }

    public Integer getActionSuccessCount(ActionSuccessCountDto dto) {
        QGame game = QGame.game;
        JobEnum actionBy = dto.getActionBy();
        NumberPath<Integer> targetCount;
        if (actionBy.equals(JobEnum.MAFIA)) {
            targetCount = game.killSuccessCount;
        } else if (actionBy.equals(JobEnum.DOCTOR)) {
            targetCount = game.cureSuccessCount;
        } else if (actionBy.equals(JobEnum.POLICE)) {
            targetCount = game.detectSuccessCount;
        } else {
            throw new IllegalArgumentException();
        }

        GameParticipationSubQueryDto gameParticipationSubQueryDto = GameParticipationSubQueryDto.builder()
                .userId(dto.getUserId())
                .job(dto.getJob())
                .build();

        return queryFactory
                .select(targetCount.sum())
                .from(game)
                .where(buildGameParticipationSubQuery(gameParticipationSubQueryDto))
                .fetchOne();
    }

    private BooleanBuilder buildSearchCondition(SearchFilter filter) {
        return new BooleanBuilder()
                .and(matchCreatedAfter(filter.getCreatedAfter()))
                .and(matchCreatedBefore(filter.getCreatedBefore()))
                .and(matchUpdatedAfter(filter.getUpdatedAfter()))
                .and(matchUpdatedBefore(filter.getUpdatedBefore()));
    }

    private BooleanBuilder buildRequestCondition(GameSearchRequest request) {
        GameParticipationSubQueryDto gameParticipationSubQueryDto = GameParticipationSubQueryDto.builder()
                .job(request.getJob())
                .userId(request.getUserId())
                .survival(request.getSurvival())
                .build();
        GameRoundSubQueryDto gameRoundSubQueryDto = GameRoundSubQueryDto.builder()
                .votedPlayerId(request.getVotedPlayerId())
                .killedPlayerId(request.getKilledPlayerId())
                .curedPlayerId(request.getCuredPlayerId())
                .detectedPlayerId(request.getDetectedPlayerId())
                .build();

        return new BooleanBuilder()
                .and(matchRoundGte(request.getRoundGte()))
                .and(matchRoundLte(request.getRoundLte()))
                .and(matchMafiaWon(request.getMafiaWon()))
                .and(matchCureSuccessCountGte(request.getCureSuccessCountGte()))
                .and(matchCureSuccessCountLte(request.getCureSuccessCountLte()))
                .and(matchKillSuccessCountGte(request.getKillSuccessCountGte()))
                .and(matchKillSuccessCountLte(request.getKillSuccessCountLte()))
                .and(matchDetectSuccessCountGte(request.getDetectSuccessCountGte()))
                .and(matchDetectSuccessCountLte(request.getDetectSuccessCountLte()))
                .and(matchCreatedAt(request.getCreatedAt()))
                .and(matchUpdatedAt(request.getUpdatedAt()))
                .and(buildGameParticipationSubQuery(gameParticipationSubQueryDto))
                .and(buildGameRoundSubQuery(gameRoundSubQueryDto));
    }

    private BooleanExpression buildGameParticipationSubQuery(GameParticipationSubQueryDto request) {
        QGame game = QGame.game;
        QGameParticipation gameParticipation = QGameParticipation.gameParticipation;

        BooleanBuilder booleanBuilder = new BooleanBuilder()
                .and(matchUserId(request.getUserId()))
                .and(matchJob(request.getJob()))
                .and(matchSurvival(request.getSurvival()));
        if (!booleanBuilder.hasValue()) {
            return null;
        }

        return game.id.in(
                queryFactory
                        .select(gameParticipation.game.id)
                        .from(gameParticipation)
                        .where(booleanBuilder)
        );
    }

    private BooleanExpression buildGameRoundSubQuery(GameRoundSubQueryDto request) {
        QGame game = QGame.game;
        QGameRound gameRound = QGameRound.gameRound;

        BooleanBuilder booleanBuilder = new BooleanBuilder()
                .and(matchVotedPlayerId(request.getVotedPlayerId()))
                .and(matchKilledPlayerId(request.getKilledPlayerId()))
                .and(matchCuredPlayerId(request.getCuredPlayerId()))
                .and(matchDetectedPlayerId(request.getDetectedPlayerId()));
        if (!booleanBuilder.hasValue()) {
            return null;
        }

        return game.id.in(
                queryFactory
                        .select(gameRound.game.id)
                        .from(gameRound)
                        .where(booleanBuilder)
        );
    }

    private BooleanExpression matchRoundGte(Integer roundGte) {
        if (roundGte == null) {
            return null;
        }
        QGame game = QGame.game;
        return game.round.goe(roundGte);
    }

    private BooleanExpression matchRoundLte(Integer roundLte) {
        if (roundLte == null) {
            return null;
        }
        QGame game = QGame.game;
        return game.round.loe(roundLte);
    }

    private BooleanExpression matchMafiaWon(Boolean mafiaWon) {
        if (mafiaWon == null) {
            return null;
        }
        QGame game = QGame.game;
        return game.mafiaWon.eq(mafiaWon);
    }

    private BooleanExpression matchCureSuccessCountGte(Integer cureSuccessCountGte) {
        if (cureSuccessCountGte == null) {
            return null;
        }
        QGame game = QGame.game;
        return game.cureSuccessCount.goe(cureSuccessCountGte);
    }

    private BooleanExpression matchCureSuccessCountLte(Integer cureSuccessCountLte) {
        if (cureSuccessCountLte == null) {
            return null;
        }
        QGame game = QGame.game;
        return game.cureSuccessCount.loe(cureSuccessCountLte);
    }

    private BooleanExpression matchKillSuccessCountGte(Integer killSuccessCountGte) {
        if (killSuccessCountGte == null) {
            return null;
        }
        QGame game = QGame.game;
        return game.killSuccessCount.goe(killSuccessCountGte);
    }

    private BooleanExpression matchKillSuccessCountLte(Integer killSuccessCountLte) {
        if (killSuccessCountLte == null) {
            return null;
        }
        QGame game = QGame.game;
        return game.killSuccessCount.loe(killSuccessCountLte);
    }

    private BooleanExpression matchDetectSuccessCountGte(Integer detectSuccessCountGte) {
        if (detectSuccessCountGte == null) {
            return null;
        }
        QGame game = QGame.game;
        return game.detectSuccessCount.goe(detectSuccessCountGte);
    }

    private BooleanExpression matchDetectSuccessCountLte(Integer detectSuccessCountLte) {
        if (detectSuccessCountLte == null) {
            return null;
        }
        QGame game = QGame.game;
        return game.detectSuccessCount.loe(detectSuccessCountLte);
    }

    private BooleanExpression matchCreatedAfter(LocalDateTime createdAfter) {
        if (createdAfter == null) {
            return null;
        }
        QGame game = QGame.game;
        return game.createdAt.after(createdAfter)
                .or(game.createdAt.eq(createdAfter));
    }

    private BooleanExpression matchCreatedBefore(LocalDateTime createdBefore) {
        if (createdBefore == null) {
            return null;
        }
        QGame game = QGame.game;
        return game.createdAt.before(createdBefore)
                .or(game.createdAt.eq(createdBefore));
    }

    private BooleanExpression matchUpdatedAfter(LocalDateTime updatedAfter) {
        if (updatedAfter == null) {
            return null;
        }
        QGame game = QGame.game;
        return game.updatedAt.after(updatedAfter)
                .or(game.updatedAt.eq(updatedAfter));
    }

    private BooleanExpression matchUpdatedBefore(LocalDateTime updatedBefore) {
        if (updatedBefore == null) {
            return null;
        }
        QGame game = QGame.game;
        return game.updatedAt.before(updatedBefore)
                .or(game.updatedAt.eq(updatedBefore));
    }

    private BooleanExpression matchCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            return null;
        }
        QGame game = QGame.game;
        LocalDateTime start = LocalDateTime.of(createdAt.toLocalDate(), LocalTime.MIN);
        LocalDateTime end = start.plusDays(1);
        return game.createdAt.between(start, end)
                .or(game.createdAt.eq(start));
    }

    private BooleanExpression matchUpdatedAt(LocalDateTime updatedAt) {
        if (updatedAt == null) {
            return null;
        }
        QGame game = QGame.game;
        LocalDateTime start = LocalDateTime.of(updatedAt.toLocalDate(), LocalTime.MIN);
        LocalDateTime end = start.plusDays(1);
        return game.updatedAt.between(start, end)
                .or(game.updatedAt.eq(start));
    }

    private BooleanExpression matchUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        QGameParticipation gameParticipation = QGameParticipation.gameParticipation;
        return gameParticipation.user.id.eq(userId);
    }

    private BooleanExpression matchJob(JobEnum job) {
        if (job == null) {
            return null;
        }
        QGameParticipation gameParticipation = QGameParticipation.gameParticipation;
        return gameParticipation.job.eq(job);
    }

    private BooleanExpression matchSurvival(Boolean survival) {
        if (survival == null) {
            return null;
        }
        QGameParticipation gameParticipation = QGameParticipation.gameParticipation;
        return gameParticipation.survival.eq(survival);
    }

    private BooleanExpression matchVotedPlayerId(Long votedPlayerId) {
        if (votedPlayerId == null) {
            return null;
        }
        QGameRound gameRound = QGameRound.gameRound;
        return gameRound.votedPlayer.id.eq(votedPlayerId);
    }

    private BooleanExpression matchKilledPlayerId(Long killedPlayerId) {
        if (killedPlayerId == null) {
            return null;
        }
        QGameRound gameRound = QGameRound.gameRound;
        return gameRound.killedPlayer.id.eq(killedPlayerId);
    }

    private BooleanExpression matchCuredPlayerId(Long curedPlayerId) {
        if (curedPlayerId == null) {
            return null;
        }
        QGameRound gameRound = QGameRound.gameRound;
        return gameRound.curedPlayer.id.eq(curedPlayerId);
    }

    private BooleanExpression matchDetectedPlayerId(Long matchDetectedPlayerId) {
        if (matchDetectedPlayerId == null) {
            return null;
        }
        QGameRound gameRound = QGameRound.gameRound;
        return gameRound.curedPlayer.id.eq(matchDetectedPlayerId);
    }
}
