package com.example.backend.controller;

import com.example.backend.model.Comment;
import com.example.backend.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addComment(@RequestBody Map<String, Object> payload) {
        try {
            String username = (String) payload.get("username");
            Long postId = Long.valueOf(payload.get("postId").toString());
            String content = (String) payload.get("content");

            Comment comment = commentService.addComment(username, postId, content);
            return ResponseEntity.ok("Comment added successfully with ID: " + comment.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editComment(@RequestBody Map<String, Object> payload) {
        try {
            Long commentId = Long.valueOf(payload.get("commentId").toString());
            String username = (String) payload.get("username");
            String newContent = (String) payload.get("newContent");

            Comment comment = commentService.editComment(commentId, username, newContent);
            return ResponseEntity.ok("Comment updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteComment(@RequestBody Map<String, Object> payload) {
        try {
            Long commentId = Long.valueOf(payload.get("commentId").toString());
            String username = (String) payload.get("username");

            commentService.deleteComment(commentId, username);
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId) {
        try {
            List<Comment> comments = commentService.getCommentsByPostId(postId);
            return ResponseEntity.ok(comments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

