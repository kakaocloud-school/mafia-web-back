package com.gg.mafia.domain.record.dao;

import com.gg.mafia.domain.record.domain.QGame;
import com.gg.mafia.domain.record.domain.QGameParticipation;
import com.gg.mafia.domain.record.dto.GameHistoryResponse;
import com.gg.mafia.domain.record.dto.QGameHistoryResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GameHistoryDao {
    private final JPAQueryFactory queryFactory;

    public Page<GameHistoryResponse> findAll(Long userId, Pageable pageable) {
        QGame game = QGame.game;
        QGameParticipation gameParticipation = QGameParticipation.gameParticipation;

        List<Long> gameParticipationIds = queryFactory
                .select(gameParticipation.id)
                .from(gameParticipation)
                .where(gameParticipation.user.id.eq(userId))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        List<GameHistoryResponse> result = queryFactory
                .select(new QGameHistoryResponse(
                        game.id,
                        game.createdAt,
                        game.updatedAt,
                        game.round,
                        game.mafiaWon,
                        gameParticipation.survival,
                        gameParticipation.job
                ))
                .from(gameParticipation)
                .innerJoin(gameParticipation.game, game)
                .where(gameParticipation.id.in(gameParticipationIds))
                .fetch();

        Long count = queryFactory
                .select(gameParticipation.count())
                .from(gameParticipation)
                .where(gameParticipation.user.id.eq(userId))
                .fetchOne();
        if (count == null) {
            count = 0L;
        }

        return new PageImpl<>(result, pageable, count);
    }
}
