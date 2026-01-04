package com.koreait.jejuguide.service;

import com.koreait.jejuguide.domain.BoardComment;
import com.koreait.jejuguide.domain.BoardPost;
import com.koreait.jejuguide.repository.BoardCommentRepository;
import com.koreait.jejuguide.repository.BoardPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardPostRepository postRepository;
    private final BoardCommentRepository commentRepository;

    public BoardServiceImpl(BoardPostRepository postRepository, BoardCommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardPost> list(String q) {
        return postRepository.search(q, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC, "id"))).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BoardPost> list(String q, int page, int size) {
        return postRepository.search(q, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    @Override
    public BoardPost addPost(String title, String content, String authorName, String authorEmail) {
        validatePost(title, content, authorName, authorEmail);
        BoardPost p = new BoardPost();
        p.setTitle(title.trim());
        p.setContent(content.trim());
        p.setAuthorName(authorName);
        p.setAuthorEmail(authorEmail);
        p.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(p);
    }

    @Override
    @Transactional
    public Optional<BoardPost> getPost(Long id, boolean increaseView) {
        Optional<BoardPost> found = postRepository.findById(id);
        found.ifPresent(p -> {
            if (increaseView) {
                p.increaseView();
                p.setUpdatedAt(LocalDateTime.now());
            }
        });
        return found;
    }

    @Override
    public BoardPost editPost(Long id, String title, String content, String requesterEmail, boolean isAdmin) {
        BoardPost post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        boolean owner = post.isAuthor(requesterEmail);
        if (!isAdmin && !owner) throw new SecurityException("수정 권한이 없습니다.");
        if (!StringUtils.hasText(title) || !StringUtils.hasText(content))
            throw new IllegalArgumentException("제목과 내용을 입력하세요.");
        post.setTitle(title.trim());
        post.setContent(content.trim());
        post.setUpdatedAt(LocalDateTime.now());
        return post;
    }

    @Override
    public void deletePost(Long id, String requesterEmail, boolean isAdmin) {
        BoardPost post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        boolean owner = post.isAuthor(requesterEmail);
        if (!isAdmin && !owner) throw new SecurityException("삭제 권한이 없습니다.");
        postRepository.delete(post);
    }

    @Override
    public BoardComment addComment(Long postId, String authorName, String authorEmail, String content) {
        if (!StringUtils.hasText(content)) throw new IllegalArgumentException("댓글을 입력하세요.");
        BoardPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        BoardComment c = new BoardComment();
        c.setPost(post);
        c.setAuthorName(authorName);
        c.setAuthorEmail(authorEmail);
        c.setContent(content.trim());
        return commentRepository.save(c);
    }

    @Override
    public BoardComment editComment(Long commentId, String requesterEmail, boolean isAdmin, String content) {
        if (!StringUtils.hasText(content)) throw new IllegalArgumentException("내용을 입력하세요.");
        BoardComment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        boolean owner = requesterEmail != null && requesterEmail.equalsIgnoreCase(c.getAuthorEmail());
        if (!isAdmin && !owner) throw new SecurityException("수정 권한이 없습니다.");
        c.setContent(content.trim());
        return c;
    }

    @Override
    public void deleteComment(Long commentId, String requesterEmail, boolean isAdmin) {
        BoardComment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        boolean owner = requesterEmail != null && requesterEmail.equalsIgnoreCase(c.getAuthorEmail());
        if (!isAdmin && !owner) throw new SecurityException("삭제 권한이 없습니다.");
        commentRepository.delete(c);
    }

    private void validatePost(String title, String content, String authorName, String authorEmail) {
        if (!StringUtils.hasText(title) || title.trim().length() < 2)
            throw new IllegalArgumentException("제목은 2자 이상 입력하세요.");
        if (!StringUtils.hasText(content) || content.trim().length() < 10)
            throw new IllegalArgumentException("내용은 10자 이상 입력하세요.");
        if (!StringUtils.hasText(authorName))
            throw new IllegalArgumentException("작성자를 입력하세요.");
        if (!StringUtils.hasText(authorEmail))
            throw new IllegalArgumentException("이메일을 입력하세요.");
    }
}
