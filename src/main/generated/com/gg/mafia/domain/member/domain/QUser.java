package com.gg.mafia.domain.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 308678246L;

    public static final QUser user = new QUser("user");

    public final com.gg.mafia.domain.model.QBaseEntity _super = new com.gg.mafia.domain.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath password = createString("password");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<com.gg.mafia.domain.achievement.domain.UserAchievement, com.gg.mafia.domain.achievement.domain.QUserAchievement> userAchievements = this.<com.gg.mafia.domain.achievement.domain.UserAchievement, com.gg.mafia.domain.achievement.domain.QUserAchievement>createList("userAchievements", com.gg.mafia.domain.achievement.domain.UserAchievement.class, com.gg.mafia.domain.achievement.domain.QUserAchievement.class, PathInits.DIRECT2);

    public final ListPath<UserToRole, QUserToRole> userToRoles = this.<UserToRole, QUserToRole>createList("userToRoles", UserToRole.class, QUserToRole.class, PathInits.DIRECT2);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

