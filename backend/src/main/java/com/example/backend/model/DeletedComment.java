package com.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deleted_comments")
public class DeletedComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "moderated_by", nullable = false)
    private User moderatedBy;

    @Column(nullable = false)
    private LocalDateTime deletedAt;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reason;
}
