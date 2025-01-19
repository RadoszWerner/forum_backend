package com.example.backend.service;

import com.example.backend.model.Comment;
import com.example.backend.model.Post;
import com.example.backend.model.User;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ToxicityCheckService toxicityCheckService;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddComment_WhenNotToxic_Success() {
        User user = new User();
        user.setUsername("user123");

        Post post = new Post();
        post.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);

        when(userRepository.findByUsername("user123")).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(toxicityCheckService.isToxic("This is a comment")).thenReturn(false);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = commentService.addComment("user123", 1L, "This is a comment");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testAddComment_WhenToxic_MarksAsDeleted() {
        User user = new User();
        user.setUsername("user123");

        Post post = new Post();
        post.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setToxic(true);
        comment.setDeleted(true);

        when(userRepository.findByUsername("user123")).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(toxicityCheckService.isToxic("This is a comment")).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = commentService.addComment("user123", 1L, "This is a comment");

        assertNotNull(result);
        assertTrue(result.isToxic());
        assertTrue(result.isDeleted());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}
