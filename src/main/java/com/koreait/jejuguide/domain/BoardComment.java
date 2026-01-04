package com.koreait.jejuguide.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="board_comments")
public class BoardComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id", nullable=false)
    private BoardPost post;

    @Column(nullable=false, length=100)
    private String authorName;

    @Column(nullable=false, length=200)
    private String authorEmail;

    @Lob @Column(nullable=false)
    private String content;

    @Column(nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    //== getter/setter ==//
    public Long getId() { return id; }
    public BoardPost getPost() { return post; }
    public void setPost(BoardPost post) { this.post = post; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getAuthorEmail() { return authorEmail; }
    public void setAuthorEmail(String authorEmail) { this.authorEmail = authorEmail; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
