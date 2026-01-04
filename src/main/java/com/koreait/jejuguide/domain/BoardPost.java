package com.koreait.jejuguide.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board_posts")
public class BoardPost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200)
    private String title;

    @Lob
    @Column(nullable=false)
    private String content;

    @Column(nullable=false, length=100)
    private String authorName;

    @Column(nullable=false, length=150)
    private String authorEmail;

    @Column(nullable=false)
    private int viewCount = 0;

    @Column(nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable=false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy="post", cascade=CascadeType.ALL, orphanRemoval=true)
    @OrderBy("id desc")
    private List<BoardComment> comments = new ArrayList<>();

    // 편의 메서드
    public boolean isAuthor(String email) {
        return email != null && email.equalsIgnoreCase(this.authorEmail);
    }
    public void increaseView() { this.viewCount = this.viewCount + 1; }
    public void addComment(BoardComment c) { comments.add(c); c.setPost(this); }

    // getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthorName() { return authorName; }
    public String getAuthorEmail() { return authorEmail; }
    public int getViewCount() { return viewCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<BoardComment> getComments() { return comments; }

    // setters
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public void setAuthorEmail(String authorEmail) { this.authorEmail = authorEmail; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
