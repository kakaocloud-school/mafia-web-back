package com.gg.mafia.domain.comment.dao;

import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.comment.domain.QComment;
import com.gg.mafia.domain.comment.dto.CommentResponse;
import com.gg.mafia.domain.comment.dto.QCommentResponse;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.profile.dao.ProfileDao;
import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.domain.profile.domain.QProfile;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/root-context.xml")
@Slf4j
//@Transactional
//@Rollback(value = false)
public class CommentDaoImplTest {
    private final Random rand = new Random();
    @Autowired
    EntityManager em;
    @Autowired
    CommentDao commentDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ProfileDao profileDao;
    User user;
    Profile profile;
    List<User> users = new ArrayList<>();
    Long userId;
    Long profileId;

    @Autowired
    JPAQueryFactory queryFactory;
    int userDataCnt = 10000;
    long commentDataCnt = 50000;

    //    @BeforeEach
    @Test
    @Disabled
    @DisplayName("user DB 추가")
    public void userSetUp() {
        for (int i = 1; i < userDataCnt; i++) {
            user = createUser(String.format("TEST_USER%d@naver.com", i), "123");
            saveToUser(user);
            users.add(user);
        }
//        createComments((int) commentDataCnt);
    }

    @Test
    @Disabled
    @DisplayName("comment DB 추가")
    public void commentSetUp() {
        createComments((int) commentDataCnt);
    }

    public void createComments(int count) {
        for (int i = 0; i < count; i++) {
            userId = (long) rand.nextInt(1, 100);
            profileId = (long) rand.nextInt(1, 100);
//            while (userId == profileId) {
//                userId = (long) rand.nextInt(1, userDataCnt);
//            }

            User user1 = userDao.findById(userId).get();
            Profile profile1 = profileDao.findById(profileId).get();
            createComment(user1, profile1, user1.getEmail() + " 코멘트");
        }
    }

    private void createComment(User user1, Profile profile1, String content) {
        Comment comment = Comment.builder().user(user1).profile(profile1).content(content).build();
        commentDao.save(comment);
    }


    @Test
    @DisplayName("comment - findComments() 테스트")
    @Transactional
    public void findCommentsTest() {
        List<Comment> comments = commentDao.findAll();
        int eSize = 2;
        Page<CommentResponse> result = commentDao.findComments(3L, PageRequest.of(0, eSize));

        Assertions.assertThat(comments.stream().filter(e -> e.getProfile().getId() == 3L).toList().size())
                .isEqualTo(result.getTotalElements());
        Assertions.assertThat(eSize).isEqualTo(result.getContent().size());
    }

    @Test
    @DisplayName("TotalQueryTest 테스트")
    public void totalQueryTest() {
        long thetaCost = 0L;
        long subQueryCost = 0L;
        for (int i = 0; i < 10; i++) {
            long cost = thetaquery((long) rand.nextInt(1, 100));
            thetaCost += cost;
            em.clear();
        }

        for (int i = 0; i < 10; i++) {
            long cost = subquery((long) rand.nextInt(1, 100));
            subQueryCost += cost;
            em.clear();
        }
        log.debug("thetaQueryTest Cost : {}", thetaCost / 10);
        log.debug("subQueryTest Cost : {}", subQueryCost / 10);
    }

    @Test
    @DisplayName("thetaQueryTest 테스트")
    public void joinTest() {
        long timeCost = 0L;
        for (int i = 0; i < 10; i++) {
            long cost = thetaquery((long) rand.nextInt(1, userDataCnt));
            System.out.println("Cost:" + cost);
            timeCost += cost;
            em.clear();
        }
        log.debug("timeCost : {}", timeCost);
    }

    @Test
    @DisplayName("subQueryTest 테스트")
    public void joinTest2() {
        long timeCost = 0L;
        for (int i = 0; i < 10; i++) {
            long cost = subquery((long) rand.nextInt(1, userDataCnt));
            System.out.println("Cost:" + cost);
            timeCost += cost;
            em.clear();
        }
        log.debug("timeCost : {}", timeCost);

    }

    @Test
    public void subQueryTest() {
        List<Comment> all = commentDao.findAll();
        QProfile subProfile = new QProfile("subProfile");
        QComment comment = QComment.comment;
        List<CommentResponse> fetch = queryFactory
                .select(new QCommentResponse(
                                JPAExpressions
                                        .select(subProfile.userName)
                                        .from(subProfile)
                                        .where(subProfile.user.id.eq(comment.user.id)), comment
                        )
                )
                .from(comment)
                .offset(0L)
                .limit(10000L)
                .where(comment.profile.id.eq(7L)).fetch();

        Assertions.assertThat(all.stream().filter(e -> e.getProfile().getId() == 7L).toList().size())
                .isEqualTo(fetch.size());
    }

    @Test
    public void thetaQueryTest() {
        List<Comment> all = commentDao.findAll();
        QProfile profile = QProfile.profile;
        QComment comment = QComment.comment;
        List<CommentResponse> fetch = queryFactory
                .select(
                        new QCommentResponse(
                                profile.userName, comment
                        )
                )
                .from(comment)
                .join(profile)
                .on(comment.user.id.eq(profile.user.id))
                .offset(0L)
                .limit(10000L)
                .where(comment.profile.id.eq(7L)).fetch();

        Assertions.assertThat(all.stream().filter(e -> e.getProfile().getId() == 7L).toList().size())
                .isEqualTo(fetch.size());
    }

    public long subquery(Long param) {
        QProfile subProfile = new QProfile("subProfile");
        QComment comment = QComment.comment;
        long startTime = System.currentTimeMillis();
        queryFactory
                .select(new QCommentResponse(
                                JPAExpressions
                                        .select(subProfile.userName)
                                        .from(subProfile)
                                        .where(subProfile.user.id.eq(comment.user.id)), comment
                        )
                )
                .from(comment)
                .offset(0L)
                .limit(10000L)
                .where(comment.profile.id.eq(param)).fetch();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    public long thetaquery(Long param) {
        QProfile profile = QProfile.profile;
        QComment comment = QComment.comment;
        long startTime = System.currentTimeMillis();
        queryFactory
                .select(
                        new QCommentResponse(
                                profile.userName, comment
                        )
                )
                .from(comment)
                .join(profile)
                .on(comment.user.id.eq(profile.user.id))
                .where(comment.profile.id.eq(param))
                .offset(0L)
                .limit(10000L)
                .fetch();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private long fetchQuery(JPAQuery<?> query) {
        long start = System.currentTimeMillis();
        query.fetch();
        long end = System.currentTimeMillis();
        return end - start;
    }

    public void saveToUser(User user) {
        profile = createProfile(user, user.getEmail() + "닉네임");
        userDao.save(user);
    }

    private Profile createProfile(User user, String userName) {
        return Profile.builder().user(user).userName(userName).build();
    }

    private User createUser(String email, String pw) {
        return User.builder().email(email).password(pw).build();
    }
}
