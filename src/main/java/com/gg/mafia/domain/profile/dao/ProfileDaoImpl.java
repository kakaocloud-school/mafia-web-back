package com.gg.mafia.domain.profile.dao;

import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.domain.profile.domain.QProfile;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
