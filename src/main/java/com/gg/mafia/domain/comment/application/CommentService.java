package com.gg.mafia.domain.comment.application;

import com.gg.mafia.domain.comment.dao.CommentDao;
import com.gg.mafia.domain.comment.dao.CommentDaoImpl;
import com.gg.mafia.domain.comment.dto.CommentRequest;

import java.util.List;

public class CommentService {
    private CommentDao commentDao = new CommentDaoImpl();
    public static void addComment(CommentRequest comment) {
        commentDao.insertComment(comment);
    }

    public void updateComment(CommentRequest comment) {
        commentDao.updateComment(comment);
    }

    public List<CommentRequest> getAllComment() {
        return commentDao.getAllComment();
    }
 
    public void deleteComment(Long id) {
        commentDao.deleteComment(id);
    }
}
