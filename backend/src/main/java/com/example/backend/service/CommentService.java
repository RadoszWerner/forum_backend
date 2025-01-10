package com.example.backend.service;

import com.example.backend.model.Comment;
import com.example.backend.model.Post;
import com.example.backend.model.User;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Comment addComment(String username, Long postId, String content) {
        // Find the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Find the post by ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // Create a new comment
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public Comment editComment(Long commentId, String username, String newContent) {
        // Find the comment by ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // Check if the username matches the comment owner
        if (!comment.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("User is not authorized to edit this comment");
        }

        // Update the comment content and timestamp
        comment.setContent(newContent);
        comment.setUpdatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }
}
