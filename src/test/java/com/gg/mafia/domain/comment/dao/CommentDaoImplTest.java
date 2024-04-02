package com.gg.mafia.domain.comment.dao;

import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.comment.dto.CommentResponse;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.profile.dao.ProfileDao;
import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.global.config.AppConfig;
import com.gg.mafia.global.config.auditing.AuditingConfig;
import com.gg.mafia.global.config.db.TestDbConfig;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class, AuditingConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@Slf4j
public class CommentDaoImplTest {
    private final Random rand;
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

    public CommentDaoImplTest() {
        this.rand = new Random();
    }

    @BeforeEach
    public void setUp() {
        for (int i = 1; i < 11; i++) {
            user = createUser(String.format("TEST_USER%d@naver.com", i), "123");
            saveToUser(user);
            users.add(user);
        }
        createComments(10);
    }

    private void createComments(int count) {
        for (int i = 0; i < count; i++) {
            userId = (long) rand.nextInt(1, users.size());
            profileId = (long) rand.nextInt(1, users.size());
            while (userId == profileId) {
                userId = (long) rand.nextInt(1, users.size());
            }

            User user1 = userDao.findById(userId).get();
            Profile profile1 = profileDao.findById(profileId).get();
            Comment comment = createComment(user1, profile1, user1.getEmail() + " 코멘트");
            commentDao.save(comment);
        }
    }

    private Comment createComment(User user1, Profile profile1, String content) {
        return Comment.builder().user(user1).profile(profile1).content(content).build();
    }


    @Test
    @DisplayName("comment - findComments() 테스트")
    @Transactional
    public void findCommentsTest() {
        List<Comment> comments = commentDao.findAll();
        comments.stream().forEach(e -> {
            log.info("comment 방명록 : {}", e.getProfile());
            log.info("comment 방명록의 유저 정보 : {}", e.getProfile().getUser());
            log.info("comment 댓글 작성자 : {}", e.getUser());
            log.info("comment 댓글 내용 : {}", e.getContent());
            log.info("==================================");
        });

        Page<CommentResponse> comments1 = commentDao.findComments(3L, PageRequest.of(0, 2));
        comments1.stream().forEach(e -> {
            log.info("방명록 : {}", e.getComment().getProfile());
            log.info("댓글 작성자 : {}", e.getUserName());
            log.info("댓글 내영 : {}", e.getComment().getContent());
        });

        Assertions.assertThat(comments.stream().filter(e -> e.getProfile().getId() == 3L).toList().size())
                .isEqualTo(comments1.get().toList().size());
    }

    private void saveToUser(User user) {
        profile = createProfile(user, user.getEmail() + "닉네임");
        user.setProfile(profile);
        userDao.save(user);
    }

    private Profile createProfile(User user, String userName) {
        return Profile.builder().user(user).userName(userName).build();
    }

    private User createUser(String email, String pw) {
        return User.builder().email(email).password(pw).build();
    }
}
