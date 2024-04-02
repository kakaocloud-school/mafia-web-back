package com.gg.mafia.domain.comment.dao;

import com.gg.mafia.domain.comment.domain.QComment;
import com.gg.mafia.domain.comment.dto.CommentResponse;
import com.gg.mafia.domain.comment.dto.QCommentResponse;
import com.gg.mafia.domain.profile.domain.QProfile;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CommentDaoImpl implements CommentDaoCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommentResponse> findComments(Long profileId, Pageable pageable) {
        QProfile profile = QProfile.profile;
        QProfile subProfile = new QProfile("subProfile");
        QComment comment = QComment.comment;

//        List<Tuple> fetch = queryFactory
//                .select(comment,
//                        JPAExpressions
//                                .select(subProfile.userName)
//                                .from(subProfile)
//                                .where(subProfile.user.id.eq(comment.user.id))
//                )
//                .from(comment)
//                .join(comment.profile, profile)
//                .where(comment.profile.id.eq(profileId)).fetch();
        List<CommentResponse> result = queryFactory
                .select(new QCommentResponse(JPAExpressions
                        .select(subProfile.userName)
                        .from(subProfile)
                        .where(subProfile.user.id.eq(comment.user.id))
                        , comment)
                )
                .from(comment)
                .join(comment.profile, profile)
                .where(comment.profile.id.eq(profileId)).fetch();

        long count = searchCount(profileId);
        return new PageImpl<>(result, pageable, count);
    }

    public Long searchCount(Long profileId) {
        QComment comment = QComment.comment;

        Long count = queryFactory
                .select(comment.count()).from(comment)
                .where(comment.profile.id.eq(profileId))
                .fetchOne();
        if (count == null) {
            count = 0L;
        }
        return count;
    }
}

