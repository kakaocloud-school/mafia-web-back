package com.gg.mafia.domain.comment.dao;

import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.profile.dao.ProfileDao;
import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.auditing.AuditingConfig;
import com.gg.mafia.global.config.db.TestDbConfig;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class, AuditingConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@Slf4j
class CommentDaoTest {
    @Autowired
    EntityManager em;
    @Autowired
    CommentDao commentDao;
    @Autowired
    ProfileDao profileDao;
    User user;
    Profile profile;

    @Test
    @DisplayName("comment - save() 테스트")
    @Transactional
    public void saveTest() {
        Comment comment = saveComment(createComment("TEST@naver.com", "123", "TEST_USER", "TEST_COMMENT"));

        Assertions.assertThat(em.contains(comment)).isTrue();
    }

    @Test
    @DisplayName("comment - findById() 테스트")
    @Transactional
    public void findByIdTest() {
        Comment comment = saveComment(createComment("TEST@naver.com", "123", "TEST_USER", "TEST_COMMENT"));

        Comment findComment = commentDao.findById(comment.getId()).get();

        Assertions.assertThat(findComment.getId()).isEqualTo(comment.getId());
    }

    @Test
    @DisplayName("comment - findByUserId() 테스트")
    @Transactional
    public void findByUserIdTest() {
        Comment comment = saveComment(createComment("TEST@naver.com", "123", "TEST_USER", "TEST_COMMENT"));

        Comment findComment = commentDao.findByUserId(comment.getUser().getId()).get();

        Assertions.assertThat(findComment.getId()).isEqualTo(comment.getId());
    }

    @Test
    @DisplayName("comment - findByProfileId() 테스트")
    @Transactional
    public void findByProfileIdTest() {
        Comment comment = saveComment(createComment("TEST@naver.com", "123", "TEST_USER", "TEST_COMMENT"));

        Comment findComment = commentDao.findByProfileId(comment.getProfile().getId()).get();

        Assertions.assertThat(findComment.getId()).isEqualTo(comment.getId());
    }

    @Test
    @DisplayName("comment - findByUserIdAndProfileId() 테스트")
    @Transactional
    public void findByUserIdAndProfileIdTest() {
        Comment comment = saveComment(createComment("TEST@naver.com", "123", "TEST_USER", "TEST_COMMENT"));

        Comment findComment = commentDao.findByUserIdAndProfileId(comment.getUser().getId(),
                comment.getProfile().getId()).get();

        Assertions.assertThat(findComment.getId()).isEqualTo(comment.getId());
    }

    @Test
    @DisplayName("comment - deleteById() 테스트")
    @Transactional
    public void deleteByIdTest() {
        Comment comment = saveComment(createComment("TEST@naver.com", "123", "TEST_USER", "TEST_COMMENT"));

        commentDao.deleteById(comment.getId());

        Assertions.assertThat(em.contains(comment)).isFalse();
    }

    @Test
    @DisplayName("comment - deleteByProfileId() 테스트")
    @Transactional
    public void deleteByProfileIdTest() {
        Comment comment = saveComment(createComment("TEST@naver.com", "123", "TEST_USER", "TEST_COMMENT"));

        commentDao.delete(comment);
        Assertions.assertThat(em.contains(comment)).isFalse();
    }

    @Test
    @DisplayName("comment - 프로필 CascadeType.REMOVE 테스트")
    @Transactional
    public void cascadeRemoveTest() {
        Comment comment = saveComment(createComment("TEST@naver.com", "123", "TEST_USER", "TEST_COMMENT"));

        profileDao.deleteById(comment.getProfile().getId());

        Assertions.assertThat(em.contains(comment.getProfile())).isFalse();
        Assertions.assertThat(em.contains(comment)).isFalse();
    }

    private Comment saveComment(Comment comment) {
        commentDao.save(comment);
        return comment;
    }

    private Comment createComment(String email, String pw, String userName, String comment) {
        user = createUser(email, pw);
        profile = createProfile(user, userName);

        return Comment.builder().user(user).profile(profile).comment(comment).build();
    }

    private Profile createProfile(User user, String userName) {
        return Profile.builder().user(user).userName(userName).build();
    }

    private User createUser(String email, String pw) {
        return User.builder().email(email).password(pw).build();
    }
}