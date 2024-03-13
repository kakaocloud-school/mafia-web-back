package com.gg.mafia.domain.record.dao;

import com.gg.mafia.domain.record.domain.Game;
import com.gg.mafia.domain.record.domain.QGame;
import com.gg.mafia.domain.record.domain.QGameParticipation;
import com.gg.mafia.domain.record.dto.GameSearchRequest;
import com.gg.mafia.global.common.request.SearchFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
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

    public Page<Game> search(GameSearchRequest request, SearchFilter filter, @NonNull Pageable pageable) {
        QGame game = QGame.game;
        List<Game> games = queryFactory
                .selectFrom(game)
                .where(buildSearchCondition(filter), buildRequestCondition(request))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        Long count = queryFactory
                .select(game.count())
                .from(game)
                .where(buildSearchCondition(filter), buildRequestCondition(request))
                .fetchOne();
        if (count == null) {
            count = 0L;
        }
        return new PageImpl<>(games, pageable, count);
    }

    private BooleanBuilder buildSearchCondition(SearchFilter filter) {
        return new BooleanBuilder()
                .and(matchCreatedAfter(filter.getCreatedAfter()))
                .and(matchCreatedBefore(filter.getCreatedBefore()))
                .and(matchUpdatedAfter(filter.getUpdatedAfter()))
                .and(matchUpdatedBefore(filter.getUpdatedBefore()));
    }

    private BooleanBuilder buildRequestCondition(GameSearchRequest request) {
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
                .and(matchUserId(request.getUserId()));
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
        QGame game = QGame.game;
        QGameParticipation gameParticipation = QGameParticipation.gameParticipation;
        return game.id.in(
                queryFactory
                        .select(gameParticipation.game.id)
                        .from(gameParticipation)
                        .where(gameParticipation.user.id.eq(userId))
        );
    }
}
