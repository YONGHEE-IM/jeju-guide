package com.koreait.jejuguide.controller;

import com.koreait.jejuguide.domain.Member;
import com.koreait.jejuguide.repository.MemberRepository;
import com.koreait.jejuguide.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@Validated
public class AccountController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public AccountController(MemberRepository memberRepository, MemberService memberService) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    /** 내 계정 화면 */
    @GetMapping("/account/me")
    public String me(Principal principal, Model model) {
        String email = principal.getName();
        Member me = memberRepository.findByEmail(email).orElseThrow();
        model.addAttribute("me", me);

        boolean isAdmin = principal != null
                && "admin@naver.com".equalsIgnoreCase(principal.getName());
        model.addAttribute("roleLabel", isAdmin ? "관리자" : "일반 사용자");
        
        return "account/me";
    }

    /** 비밀번호 변경 */
    @PostMapping("/account/password")
    public String changePassword(
            Principal principal,
            @RequestParam("currentPassword") @NotBlank String currentPassword,
            @RequestParam("newPassword")     @NotBlank String newPassword,
            Model model
    ) {
        String email = principal.getName();
        try {
            memberService.changePassword(email, currentPassword, newPassword);
            model.addAttribute("pwChanged", true);
        } catch (IllegalArgumentException e) {
            model.addAttribute("pwError", e.getMessage());
        }
        Member me = memberRepository.findByEmail(email).orElseThrow();
        model.addAttribute("me", me);
        return "account/me";
    }

    /** 계정 삭제 */
    @PostMapping("/account/delete")
    public String deleteAccount(
            Principal principal,
            HttpServletRequest request,
            @RequestParam("currentPassword") @NotBlank String currentPassword,
            Model model
    ) {
        String email = principal.getName();
        try {
            memberService.deleteAccount(email, currentPassword);
        } catch (IllegalArgumentException e) {
            Member me = memberRepository.findByEmail(email).orElseThrow();
            model.addAttribute("me", me);
            model.addAttribute("deleteError", e.getMessage());
            return "account/me";
        }
        // 세션/인증정보 정리
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/login?deleted=1";
    }
}
