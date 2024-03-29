package com.gg.mafia.domain.comment.application;

import com.gg.mafia.domain.comment.dao.CommentDao;
import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.profile.dao.ProfileDao;
import com.gg.mafia.domain.profile.domain.Profile;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentDao commentDao;
    private final ProfileDao profileDao;
    private final UserDao userDao;

    @Transactional
    public List<Comment> selectCommentByProfileId(Long profileId) {
        return commentDao.findByProfileId(profileId);
    }

    @Transactional
    public void insertComment(String userEmail, Long profileId, String content) {
        User user = getUserByEmail(userEmail);
        Profile profile = getProfileById(profileId);
        Comment comment = Comment.builder().user(user).profile(profile).content(content).build();
        commentDao.save(comment);
    }

    @Transactional
    public void updateComment(String userEmail, Long profileId, String updateContent) {
        User user = getUserByEmail(userEmail);
        Comment comment = getCommentByUserIdAndProfileId(user.getId(), profileId);
        comment.updateContent(updateContent);
        commentDao.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentDao.deleteById(commentId);
    }

    private User getUserByEmail(String userEmail) {
        return userDao.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException());
    }

    private Profile getProfileById(Long profileId) {
        return profileDao.findById(profileId).orElseThrow(() -> new IllegalArgumentException());
    }

    private Comment getCommentByUserIdAndProfileId(Long userId, Long profileId) {
        return commentDao.findByUserIdAndProfileId(userId, profileId).orElseThrow(() -> new IllegalArgumentException());
    }

    private List<Comment> getCommentByProfileId(Long profiletId) {
        return commentDao.findByProfileId(profiletId);
    }

    private List<Comment> getCommentByUserId(Long userId) {
        return commentDao.findByUserId(userId);
    }


}
