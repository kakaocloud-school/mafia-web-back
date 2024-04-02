package com.gg.mafia.domain.comment.application;

import com.gg.mafia.domain.comment.dao.CommentDao;
import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.comment.dto.CommentResponse;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.profile.dao.ProfileDao;
import com.gg.mafia.domain.profile.domain.Profile;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentDao commentDao;
    private final ProfileDao profileDao;
    private final UserDao userDao;

    @Transactional
    public Page<CommentResponse> selectComments(Long profileId, Pageable pageable) {
        return commentDao.findComments(profileId, pageable);
    }

    @Transactional
    public Comment insertComment(String userEmail, Long profileId, String content) {
        User user = getUserByEmail(userEmail);
        Profile profile = getProfileById(profileId);
        Comment comment = createComment(user, profile, content);
        return commentDao.save(comment);
    }

    @Transactional
    public void updateComment(String userEmail, Long commentId, String updateContent) {
        User user = getUserByEmail(userEmail);
        Comment comment = getCommentByUserIdAndCommentId(user.getId(), commentId);
        comment.updateContent(updateContent);
        commentDao.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentDao.deleteById(commentId);
    }

    private Comment createComment(User user, Profile profile, String content) {
        return Comment.builder().user(user).profile(profile).content(content).build();
    }

    private User getUserByEmail(String userEmail) {
        return userDao.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException());
    }

    private Profile getProfileById(Long profileId) {
        return profileDao.findById(profileId).orElseThrow(() -> new IllegalArgumentException());
    }

    private List<Comment> getCommentByUserIdAndProfileId(Long userId, Long profileId) {
        return commentDao.findByUserIdAndProfileId(userId, profileId);
    }

    private Comment getCommentByUserIdAndCommentId(Long userId, Long commentId) {
        return commentDao.findByUserIdAndId(userId, commentId).orElseThrow(() -> new IllegalArgumentException());
    }

    private List<Comment> getCommentByProfileId(Long profiletId) {
        return commentDao.findByProfileId(profiletId);
    }

    private List<Comment> getCommentByUserId(Long userId) {
        return commentDao.findByUserId(userId);
    }


}
