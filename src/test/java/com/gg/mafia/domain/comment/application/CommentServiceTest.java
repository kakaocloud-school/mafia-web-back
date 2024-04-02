package com.gg.mafia.domain.comment.application;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.gg.mafia.domain.comment.dao.CommentDao;
import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.profile.dao.ProfileDao;
import com.gg.mafia.domain.profile.domain.Profile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@DisplayName("댓글 서비스 테스트")
@Slf4j
class CommentServiceTest {
    @InjectMocks
    CommentService service;
    @Mock
    CommentDao commentDao;
    @Mock
    UserDao userDao;
    @Mock
    ProfileDao profileDao;
    @Mock
    User user;
    @Mock
    Profile profile;
    @Mock
    Comment comment;
    List<Comment> comments = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        user = Mockito.spy(createUser("TEST@naver.com", "123"));
        profile = Mockito.spy(createProfile(user, user.getEmail() + "_USER_NAME"));
        comment = Mockito.spy(createComment(user, profile, "TEST_CONTENT"));
    }

    @Test
    @DisplayName("댓글 조회 기능 테스트")
    @Transactional
    public void selectCommentByProfileId_Test() {
//        // Given
//        createDummyCommentByProfile(comment, 10);
//
//        List<Comment> expectComments = comments.stream()
//                .filter(e -> e.getProfile().getUserName().equals(profile.getUserName()))
//                .toList();
//
//        // When
//        given(commentDao.findByProfileId(profile.getId())).willReturn(expectComments);
//
//        List<Comment> findComments = service.selectComments(profile.getId());
//
//        Assertions.assertThat(findComments.size()).isEqualTo(expectComments.size());
//
//        // THEN
//        then(commentDao).should().findByProfileId(profile.getId());
    }

    @Test
    @DisplayName("댓글 저장 기능 테스트")
    @Transactional
    public void insertComment_Test() {
        // Given
        Comment saveToContent = createComment(user, profile, "TEST_CONTENT");
        given(userDao.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(profileDao.findById(1L)).willReturn(Optional.of(profile));
        given(commentDao.save(saveToContent)).willReturn(saveToContent);

        // WHEN
        Comment savedComment = service.insertComment(user.getEmail(), 1L, saveToContent.getContent());

        Assertions.assertThat(savedComment.getContent()).isEqualTo(saveToContent.getContent());

        // THEN
        then(commentDao).should().save(saveToContent);
    }

    @Test
    @DisplayName("댓글 업데이트 기능 테스트")
    @Transactional
    public void updateComment_Test() {
        // Given
        given(user.getId()).willReturn(1L);
        given(userDao.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(commentDao.findByUserIdAndId(1L, 1L)).willReturn(Optional.of(comment));

        // WHEN
        service.updateComment(user.getEmail(), 1L, "TEST_NEW_CONTENT");

        comment.updateContent("TEST_NEW_CONTENT");

        // THEN
        then(commentDao).should().save(comment);

    }

    @Test
    @DisplayName("댓글 삭제 기능 테스트")
    @Transactional
    public void deleteComment_Test() {
        // Given
        Long commentId = 1L;
        // WHEN
        service.deleteComment(commentId);
        // THEN
        then(commentDao).should().deleteById(anyLong());
    }

    private void createDummyCommentByProfile(Comment addedComment, int count) {
        for (int i = 1; i < count; i++) {
            User user = createUser(UUID.randomUUID().toString().substring(0, 5), "123");
            Profile profile = createProfile(user, "TEST_USER_NAME");
            comment = createComment(user, profile, "TEST_CONTENT");
            comments.add(comment);
        }
        comments.add(addedComment);
    }

    private Comment createComment(User user, Profile profile, String content) {
        return Comment.builder().user(user).profile(profile).content(content).build();
    }

    private Profile createProfile(User user, String userName) {
        return Profile.builder().user(user).userName(userName).build();
    }

    private User createUser(String email, String pw) {
        return User.builder().email(email).password(pw).build();
    }
}