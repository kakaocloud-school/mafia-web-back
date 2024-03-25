package com.gg.mafia.domain.comment.application;

import com.gg.mafia.domain.Profile.domain.ProfileEntity;
import com.gg.mafia.domain.comment.dao.CommentDao;
import com.gg.mafia.domain.comment.dao.CommentDaoImpl;
import com.gg.mafia.domain.comment.dto.CommentMapper;
import com.gg.mafia.domain.comment.dto.CommentRequest;
import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.comment.dto.CommentResponse;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentService {
    private CommentDao commentDao;
    private CommentMapper commentMapper;

    //user id로 Commnet 가져온다.
    public CommentResponse getByUserId(Long id){
        Comment comment = findById(id);

        return CommentMapper.toCommentResponse(comment);
    }

    // id검색으로 comment 가져오기
    public Comment findById(Long id) {
        Optional<Comment> OCommentEntity = CommentDao.findById(id);
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);


        if (OCommentEntity.isPresent()) {
            return OCommentEntity.get();
        } else {
            throw new EntityNotFoundException("존재하지 않는 리소스");
        }
    }

    // writeid 검색으로 comment 가져오기
    public CommentResponse getByUserName(String name){
        Comment comment= findByUserName(name);

    }

    public static void addComment(CommentRequest comment) {
       // commentDao.insertComment(comment);
    }

    public void updateComment(CommentRequest comment) {
       // commentDao.updateComment(comment);
    }

   public List<CommentRequest> getAllComment() {
       // return commentDao.getAllComment();
    }
 
    public void deleteComment(Long id) {
       // commentDao.deleteComment(id);
    }
}
