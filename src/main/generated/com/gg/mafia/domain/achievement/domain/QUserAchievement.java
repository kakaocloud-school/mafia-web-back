package com.gg.mafia.domain.achievement.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserAchievement is a Querydsl query type for UserAchievement
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAchievement extends EntityPathBase<UserAchievement> {

    private static final long serialVersionUID = -1604476514L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserAchievement userAchievement = new QUserAchievement("userAchievement");

    public final com.gg.mafia.domain.model.QBaseEntity _super = new com.gg.mafia.domain.model.QBaseEntity(this);

    public final QAchievement achievement;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.gg.mafia.domain.member.domain.QUser user;

    public QUserAchievement(String variable) {
        this(UserAchievement.class, forVariable(variable), INITS);
    }

    public QUserAchievement(Path<? extends UserAchievement> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserAchievement(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserAchievement(PathMetadata metadata, PathInits inits) {
        this(UserAchievement.class, metadata, inits);
    }

    public QUserAchievement(Class<? extends UserAchievement> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.achievement = inits.isInitialized("achievement") ? new QAchievement(forProperty("achievement")) : null;
        this.user = inits.isInitialized("user") ? new com.gg.mafia.domain.member.domain.QUser(forProperty("user")) : null;
    }

}

