package com.gg.mafia.domain.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserToRole is a Querydsl query type for UserToRole
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserToRole extends EntityPathBase<UserToRole> {

    private static final long serialVersionUID = 1853449111L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserToRole userToRole = new QUserToRole("userToRole");

    public final com.gg.mafia.domain.model.QBaseEntity _super = new com.gg.mafia.domain.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QRole role;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUser user;

    public QUserToRole(String variable) {
        this(UserToRole.class, forVariable(variable), INITS);
    }

    public QUserToRole(Path<? extends UserToRole> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserToRole(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserToRole(PathMetadata metadata, PathInits inits) {
        this(UserToRole.class, metadata, inits);
    }

    public QUserToRole(Class<? extends UserToRole> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.role = inits.isInitialized("role") ? new QRole(forProperty("role")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

