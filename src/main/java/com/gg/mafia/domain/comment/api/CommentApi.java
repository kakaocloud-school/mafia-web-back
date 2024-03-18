package com.gg.mafia.domain.comment.api;

import com.gg.mafia.domain.comment.application.CommentService;
import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.comment.dto.CommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/comment")
public class CommentApi {
    @Autowired
    private CommentService commentService;

    @PostMapping("/commentadd")
    public ResponseEntity<String> addComment(@RequestBody CommentRequest comment){
        commentService.addComment(comment);

        return ResponseEntity.ok("Comment added successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateComment (@RequestBody CommentRequest comment){
        commentService.updateComment(comment);

        return ResponseEntity.ok("Comment updated successfully");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteComment (@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentRequest>> getAllComment(){
        List<CommentRequest> comments= commentService.getAllComment();

        return ResponseEntity.ok(comments);
    }

}
