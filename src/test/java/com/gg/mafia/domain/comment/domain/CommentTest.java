package com.gg.mafia.domain.comment.domain;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.auditing.AuditingConfig;
import com.gg.mafia.global.config.db.TestDbConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class, AuditingConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("댓글 테스트")
@Slf4j
class CommentTest {
    private final Random rand;
    @PersistenceUnit
    private EntityManagerFactory emf;
    private EntityManager em;
    private Comment comment;
    private User user;
    private Profile profile;

    public CommentTest() {
        this.rand = new Random();
    }

    @BeforeEach
    public void beforeAll() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        user = createUser("TEST_USER@naver.com", "123");
        profile = createProfile(user, "홍길동");
    }

    @AfterEach
    public void afterEach() {
        em.getTransaction().rollback();
        em.clear();
    }

    @Test
    @DisplayName("Comment 조회 및 생성 테스트")
    public void selectAndCreateComment() {
        comment = Comment.builder().comment("댓글 내용").user(user).profile(profile).build();

        em.persist(comment);
        em.clear();

        Comment findComment = em.find(Comment.class, comment.getId());

        Assertions.assertThat(comment.getId()).isEqualTo(findComment.getId());
        Assertions.assertThat(comment.getUser().getEmail()).isEqualTo(findComment.getUser().getEmail());
        Assertions.assertThat(comment.getProfile().getUserName()).isEqualTo(findComment.getProfile().getUserName());
    }

    @Test
    @DisplayName("Comment 업데이트 테스트")
    public void updateComment() {
        comment = Comment.builder().comment("댓글 내용").user(user).profile(profile).build();
        em.persist(comment);
        em.flush();
        em.clear();

        comment.updateComment("변경 후 댓글 내용");
        int result = em.createQuery("update Comment c set c.comment =:newComment where c.id =:commentId")
                .setParameter("newComment", comment.getComment())
                .setParameter("commentId", comment.getId())
                .executeUpdate();

        em.clear();

        Comment findComment = em.find(Comment.class, comment.getId());

        Assertions.assertThat(comment.getId()).isEqualTo(findComment.getId());
        Assertions.assertThat(comment.getComment()).isEqualTo(findComment.getComment());
    }

    @Test
    @DisplayName("Comment 삭제 테스트")
    public void removeComment() {
        comment = Comment.builder().comment("댓글 내용").user(user).profile(profile).build();
        em.persist(comment);
        em.flush();
        em.createQuery("delete from Comment c where c.id =:commentId")
                .setParameter("commentId", comment.getId())
                .executeUpdate();

        Long singleResult = (Long) em.createQuery("select count(*) from Comment c")
                .getSingleResult();

        Assertions.assertThat(singleResult).isEqualTo(0);

    }

    private Profile createProfile(User user, String userName) {
        return Profile.builder().user(user).userName(userName).build();
    }

    private User createUser(String email, String pw) {
        return User.builder().email(email).password(pw).build();
    }
}