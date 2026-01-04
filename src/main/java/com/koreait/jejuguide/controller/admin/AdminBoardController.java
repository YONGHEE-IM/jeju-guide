package com.koreait.jejuguide.controller.admin;

import com.koreait.jejuguide.domain.BoardPost;
import com.koreait.jejuguide.service.BoardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/boards")
public class AdminBoardController {

    private final BoardService boardService;

    public AdminBoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    private boolean isAdmin(User principal) {
        return principal != null && "admin@naver.com".equalsIgnoreCase(principal.getUsername());
    }

    @GetMapping
    public String list(@AuthenticationPrincipal User principal, Model model) {
        if (!isAdmin(principal)) return "redirect:/login";
        List<BoardPost> boards = boardService.list(null);
        model.addAttribute("boards", boards);
        return "admin/boards";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, @AuthenticationPrincipal User principal) {
        if (!isAdmin(principal)) return "redirect:/login";
        // 관리자 권한으로 삭제 위임
        boardService.deletePost(id, principal.getUsername(), true);
        return "redirect:/admin/boards?deleted=1";
    }
    
 // 수정 화면 (GET)
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id,
                           @AuthenticationPrincipal User principal,
                           Model model) {
        String username = principal != null ? principal.getUsername() : null;
        boolean isAdmin = "admin@naver.com".equalsIgnoreCase(username);

        // 조회수 증가 없이 게시글 조회
        BoardPost post = boardService.getPost(id, false)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 권한 체크: 작성자 또는 관리자만
        if (!isAdmin && !post.isAuthor(username)) {
            return "redirect:/boards/" + id + "?error=forbidden";
        }

        model.addAttribute("post", post);
        return "board/edit"; // 이미 존재하는 템플릿
    }

    // 수정 저장 (POST)
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id,
                       @RequestParam("title") String title,
                       @RequestParam("content") String content,
                       @AuthenticationPrincipal User principal,
                       RedirectAttributes ra) {
        String username = principal != null ? principal.getUsername() : null;
        boolean isAdmin = "admin@naver.com".equalsIgnoreCase(username);

        boardService.editPost(id, title, content, username, isAdmin);

        ra.addFlashAttribute("message", "게시글이 수정되었습니다.");
        return "redirect:/boards/" + id;
    }
}
