package com.gg.mafia.domain.profile.dao;

import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.domain.profile.domain.QProfile;
import com.gg.mafia.domain.profile.dto.ProfileSearchRequest;
import com.gg.mafia.global.common.request.SearchQuery;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@RequiredArgsConstructor
public class ProfileDaoImpl implements ProfileDaoCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Profile> findAllRanks(Pageable pageable) {
        QProfile profile = QProfile.profile;
        List<Long> ids = queryFactory.select(profile.id)
                .from(profile)
                .orderBy(getOrderByCondition(pageable))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        List<Profile> profiles = queryFactory
                .selectFrom(profile)
                .where(profile.id.in(ids))
                .orderBy(getOrderByFieldsCondition(profile.id, ids))
                .fetch();

        Long count = queryFactory.select(profile.count())
                .from(profile)
                .fetchOne();
        if (count == null) {
            count = 0L;
        }

        return new PageImpl<>(profiles, pageable, count);
    }

    @Override
    public Page<Profile> search(ProfileSearchRequest request, SearchQuery searchQuery, Pageable pageable) {
        QProfile profile = QProfile.profile;
        List<Profile> profiles = queryFactory
                .selectFrom(profile)
                .where(buildSearchCondition(searchQuery), buildRequestCondition(request))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        Long count = queryFactory
                .select(profile.count())
                .from(profile)
                .where(buildSearchCondition(searchQuery), buildRequestCondition(request))
                .fetchOne();
        if (count == null) {
            count = 0L;
        }
        return new PageImpl<>(profiles, pageable, count);
    }

    private BooleanBuilder buildSearchCondition(SearchQuery searchQuery) {
        return new BooleanBuilder()
                .and(matchKeyword(searchQuery.getKeyword()))
                .and(matchCreatedAfter(searchQuery.getCreatedAfter()))
                .and(matchCreatedBefore(searchQuery.getCreatedBefore()))
                .and(matchUpdatedAfter(searchQuery.getUpdatedAfter()))
                .and(matchUpdatedBefore(searchQuery.getUpdatedBefore()));
    }

    private BooleanBuilder buildRequestCondition(ProfileSearchRequest request) {
        return new BooleanBuilder()
                .and(matchUserName(request.getUserName()))
                .and(matchDescription(request.getDescription()))
                .and(matchCreatedAt(request.getCreatedAt()))
                .and(matchUpdatedAt(request.getUpdatedAt()));
    }

    private BooleanExpression matchKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        QProfile profile = QProfile.profile;
        return profile.userName.containsIgnoreCase(keyword)
                .or(profile.description.containsIgnoreCase(keyword));
    }

    private BooleanExpression matchCreatedAfter(LocalDateTime createdAfter) {
        if (createdAfter == null) {
            return null;
        }
        QProfile profile = QProfile.profile;
        return profile.createdAt.after(createdAfter)
                .or(profile.createdAt.eq(createdAfter));
    }

    private BooleanExpression matchCreatedBefore(LocalDateTime createdBefore) {
        if (createdBefore == null) {
            return null;
        }
        QProfile profile = QProfile.profile;
        return profile.createdAt.before(createdBefore)
                .or(profile.createdAt.eq(createdBefore));
    }

    private BooleanExpression matchUpdatedAfter(LocalDateTime updatedAfter) {
        if (updatedAfter == null) {
            return null;
        }
        QProfile profile = QProfile.profile;
        return profile.updatedAt.after(updatedAfter)
                .or(profile.updatedAt.eq(updatedAfter));
    }

    private BooleanExpression matchUpdatedBefore(LocalDateTime updatedBefore) {
        if (updatedBefore == null) {
            return null;
        }
        QProfile profile = QProfile.profile;
        return profile.updatedAt.before(updatedBefore)
                .or(profile.updatedAt.eq(updatedBefore));
    }

    private BooleanExpression matchUserName(String userName) {
        if (userName == null) {
            return null;
        }
        QProfile profile = QProfile.profile;
        return profile.userName.eq(userName);
    }

    private BooleanExpression matchDescription(String description) {
        if (description == null) {
            return null;
        }
        QProfile profile = QProfile.profile;
        return profile.description.eq(description);
    }

    private BooleanExpression matchCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            return null;
        }
        QProfile profile = QProfile.profile;
        LocalDateTime start = LocalDateTime.of(createdAt.toLocalDate(), LocalTime.MIN);
        LocalDateTime end = start.plusDays(1);
        return profile.createdAt.between(start, end)
                .or(profile.createdAt.eq(start));
    }

    private BooleanExpression matchUpdatedAt(LocalDateTime updatedAt) {
        if (updatedAt == null) {
            return null;
        }
        QProfile profile = QProfile.profile;
        LocalDateTime start = LocalDateTime.of(updatedAt.toLocalDate(), LocalTime.MIN);
        LocalDateTime end = start.plusDays(1);
        return profile.updatedAt.between(start, end)
                .or(profile.updatedAt.eq(start));
    }

    private OrderSpecifier<?> getOrderByCondition(Pageable pageable) {
        Sort sort = pageable.getSort();
        Optional<Order> order = sort.get().findFirst();
        String orderBy = order.map(Order::getProperty).orElse(null);
        OrderSpecifier<?> condition = QProfile.profile.winningRate.desc();
        if (orderBy == null) {
            condition = QProfile.profile.winningRate.desc();
        } else if (orderBy.equals(RANK_CITIZEN)) {
            condition = QProfile.profile.citizenWinningRate.desc();
        } else if (orderBy.equals(RANK_MAFIA)) {
            condition = QProfile.profile.mafiaWinningRate.desc();
        } else if (orderBy.equals(RANK_DOCTOR)) {
            condition = QProfile.profile.doctorWinningRate.desc();
        } else if (orderBy.equals(RANK_POLICE)) {
            condition = QProfile.profile.policeWinningRate.desc();
        } else if (orderBy.equals(RANK_RATING)) {
            condition = QProfile.profile.rating.desc();
        }
        return condition;
    }

    private OrderSpecifier<?> getOrderByFieldsCondition(Path<?> field, List<Long> fields) {
        String template = "FIELD({0}, %s)".formatted(
                fields.stream().map(Object::toString).collect(Collectors.joining(", "))
        );
        return Expressions.stringTemplate(template, field).asc();
    }
}
