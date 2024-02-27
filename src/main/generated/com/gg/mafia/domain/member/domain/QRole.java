package com.gg.mafia.domain.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRole is a Querydsl query type for Role
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRole extends EntityPathBase<Role> {

    private static final long serialVersionUID = 308585233L;

    public static final QRole role = new QRole("role");

    public final com.gg.mafia.domain.model.QBaseEntity _super = new com.gg.mafia.domain.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<UserToRole, QUserToRole> userToRoles = this.<UserToRole, QUserToRole>createList("userToRoles", UserToRole.class, QUserToRole.class, PathInits.DIRECT2);

    public final EnumPath<com.gg.mafia.domain.model.RoleEnum> value = createEnum("value", com.gg.mafia.domain.model.RoleEnum.class);

    public QRole(String variable) {
        super(Role.class, forVariable(variable));
    }

    public QRole(Path<? extends Role> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRole(PathMetadata metadata) {
        super(Role.class, metadata);
    }

}

