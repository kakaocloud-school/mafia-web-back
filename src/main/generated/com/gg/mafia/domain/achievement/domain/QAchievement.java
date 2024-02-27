package com.gg.mafia.domain.achievement.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAchievement is a Querydsl query type for Achievement
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAchievement extends EntityPathBase<Achievement> {

    private static final long serialVersionUID = 1642876777L;

    public static final QAchievement achievement = new QAchievement("achievement");

    public final com.gg.mafia.domain.model.QBaseEntity _super = new com.gg.mafia.domain.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAchievement(String variable) {
        super(Achievement.class, forVariable(variable));
    }

    public QAchievement(Path<? extends Achievement> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAchievement(PathMetadata metadata) {
        super(Achievement.class, metadata);
    }

}

