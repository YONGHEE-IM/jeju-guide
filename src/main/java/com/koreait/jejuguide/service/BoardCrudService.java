package com.koreait.jejuguide.service;

import com.koreait.jejuguide.domain.BoardComment;
import com.koreait.jejuguide.domain.BoardPost;
import com.koreait.jejuguide.repository.BoardCommentRepository;
import com.koreait.jejuguide.repository.BoardPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Transactional
public class BoardCrudService {

    private final BoardPostRepository postRepository;
    private final BoardCommentRepository commentRepository;

    // 🔧 final 필드 초기화를 위한 생성자 추가(없어서 컴파일/빈 생성 오류 발생 가능)
    public BoardCrudService(BoardPostRepository postRepository,
                            BoardCommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    /** addPost: 게시글 추가 (제목 2자, 내용 10자 검증) */
    public BoardPost addPost(String title, String content,
                             String authorName, String authorEmail) {
        validatePost(title, content, authorName, authorEmail);

        BoardPost p = new BoardPost();
        p.setTitle(title.trim());
        p.setContent(content);
        p.setAuthorName(authorName);
        p.setAuthorEmail(authorEmail);
        return postRepository.save(p);
    }

    /** getPost: 게시글 조회 (increaseView=true면 조회수 자동 증가) */
    @Transactional(readOnly = true)
    public Optional<BoardPost> getPost(Long id, boolean increaseView) {
        Optional<BoardPost> found = postRepository.findById(id);
        if (found.isPresent() && increaseView) {
            // 조회수 증가는 쓰기 트랜잭션이 필요하므로 별도 메서드로 위임
            increaseViewCount(id);
        }
        return found;
    }

    /** 내부용: 조회수 증가 */
    @Transactional
    public void increaseViewCount(Long id) {
        BoardPost post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        post.increaseView(); // 엔티티 메서드로 +1
        // JPA가 트랜잭션 종료 시 flush
    }

    /** addComment: 댓글 추가 (내용 공백 불가) */
    public BoardComment addComment(Long postId, String authorName,
                                   String authorEmail, String content) {
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("댓글을 입력하세요.");
        }
        BoardPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        BoardComment c = new BoardComment();
        c.setPost(post);
        c.setAuthorName(authorName);
        c.setAuthorEmail(authorEmail);
        c.setContent(content);
        return commentRepository.save(c);
    }

    /** deletePost: 작성자 또는 관리자 권한 검증 후 삭제 */
    public void deletePost(Long id, String requesterName, boolean isAdmin) {
        BoardPost post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!isAdmin && (requesterName == null || !requesterName.equals(post.getAuthorName()))) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }
        postRepository.delete(post);
    }

    /** (권한 없는 즉시 삭제가 필요할 때 사용하는 오버로드 — 선택) */
    public void deletePost(Long id) {
        BoardPost post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        postRepository.delete(post);
    }

    /** 유효성 검증 */
    private void validatePost(String title, String content,
                              String authorName, String authorEmail) {
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
