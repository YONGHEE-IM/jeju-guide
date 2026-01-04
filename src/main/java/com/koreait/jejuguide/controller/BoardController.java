package com.koreait.jejuguide.controller;

import com.koreait.jejuguide.domain.BoardPost;
import com.koreait.jejuguide.dto.CommentForm;
import com.koreait.jejuguide.dto.PostForm;
import com.koreait.jejuguide.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 목록 (페이징 5개)
    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Model model) {
        int size = 5;
        Page<BoardPost> posts = boardService.list(q, page, size);
        model.addAttribute("posts", posts);
        model.addAttribute("q", q);
        return "board/list";
    }

    // 상세 (조회수 증가)
    @GetMapping("/{id}")
    public String detail(@PathVariable("id")  Long id, Model model) {
        BoardPost post = boardService.getPost(id, true)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        model.addAttribute("post", post);
        model.addAttribute("commentForm", new CommentForm());
        return "board/detail";
    }

    // 글 작성 폼
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", new PostForm());
        return "board/create";
    }

    // 글 등록
    @PostMapping
    public String create(@Valid @ModelAttribute("form") PostForm form,
                         @AuthenticationPrincipal User principal,
                         RedirectAttributes ra) {
        String username = principal != null ? principal.getUsername() : "anonymous";
        boardService.addPost(form.getTitle(), form.getContent(), username, username);
        ra.addFlashAttribute("message", "게시글이 등록되었습니다.");
        return "redirect:/boards";
    }

    // 글 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id,
                           @AuthenticationPrincipal User principal,
                           Model model) {
        BoardPost post = boardService.getPost(id, false)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        String username = principal != null ? principal.getUsername() : "anonymous";
        boolean isOwner = username.equalsIgnoreCase(post.getAuthorEmail());
        boolean isAdmin = "admin@naver.com".equalsIgnoreCase(username);
        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        PostForm form = new PostForm();
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());
        model.addAttribute("form", form);
        model.addAttribute("post", post);
        return "board/edit";
    }

    // 글 수정 처리
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id,
			            @Valid @ModelAttribute("form") PostForm form,
			            @AuthenticationPrincipal User principal,
			            RedirectAttributes ra) {
        String username = principal != null ? principal.getUsername() : "anonymous";
        boolean isAdmin = "admin@naver.com".equalsIgnoreCase(username);
        boardService.editPost(id, form.getTitle(), form.getContent(), username, isAdmin);
        ra.addFlashAttribute("message", "게시글이 수정되었습니다.");
        return "redirect:/boards/" + id;
    }

    // 글 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id,
            			 @AuthenticationPrincipal User principal,
                         RedirectAttributes ra) {
        String username = principal != null ? principal.getUsername() : "anonymous";
        boolean isAdmin = "admin@naver.com".equalsIgnoreCase(username);
        boardService.deletePost(id, username, isAdmin);
        ra.addFlashAttribute("message", "게시글이 삭제되었습니다.");
        return "redirect:/boards";
    }

    // 댓글 등록
    @PostMapping("/{postId}/comments")
    public String addComment(@PathVariable("postId") Long postId,
				            @Valid @ModelAttribute("commentForm") CommentForm form,
				            @AuthenticationPrincipal User principal,
                             RedirectAttributes ra) {
        String username = principal != null ? principal.getUsername() : "anonymous";
        boardService.addComment(postId, username, username, form.getContent());
        ra.addFlashAttribute("message", "댓글이 등록되었습니다.");
        return "redirect:/boards/" + postId;
    }

    // 댓글 수정
    @PostMapping("/{postId}/comments/{commentId}/edit")
    public String editComment(@PathVariable("postId") Long postId,
					          @PathVariable("commentId") Long commentId,
					          @RequestParam("content") String content,
					          @AuthenticationPrincipal User principal,
                              RedirectAttributes ra) {
        String username = principal != null ? principal.getUsername() : "anonymous";
        boolean isAdmin = "admin@naver.com".equalsIgnoreCase(username);
        boardService.editComment(commentId, username, isAdmin, content);
        ra.addFlashAttribute("message", "댓글이 수정되었습니다.");
        return "redirect:/boards/" + postId;
    }

    // 댓글 삭제
    @PostMapping("/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("postId") Long postId,
					            @PathVariable("commentId") Long commentId,
					            @AuthenticationPrincipal User principal,
                                RedirectAttributes ra) {
        String username = principal != null ? principal.getUsername() : "anonymous";
        boolean isAdmin = "admin@naver.com".equalsIgnoreCase(username);
        boardService.deleteComment(commentId, username, isAdmin);
        ra.addFlashAttribute("message", "댓글이 삭제되었습니다.");
        return "redirect:/boards/" + postId;
    }
}
