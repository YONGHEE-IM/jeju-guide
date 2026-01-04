package com.koreait.jejuguide.service;

import com.koreait.jejuguide.domain.BoardComment;
import com.koreait.jejuguide.domain.BoardPost;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BoardService {
    // 목록
    List<BoardPost> list(String q);
    Page<BoardPost> list(String q, int page, int size);

    // 게시글
    BoardPost addPost(String title, String content, String authorName, String authorEmail);
    Optional<BoardPost> getPost(Long id, boolean increaseView);
    BoardPost editPost(Long id, String title, String content, String requesterEmail, boolean isAdmin);
    void deletePost(Long id, String requesterEmail, boolean isAdmin);

    // 댓글
    BoardComment addComment(Long postId, String authorName, String authorEmail, String content);
    BoardComment editComment(Long commentId, String requesterEmail, boolean isAdmin, String content);
    void deleteComment(Long commentId, String requesterEmail, boolean isAdmin);
}
