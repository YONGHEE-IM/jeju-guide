package com.koreait.jejuguide.controller;

import com.koreait.jejuguide.dto.JoinForm;
import com.koreait.jejuguide.service.MemberService;
import jakarta.validation.Valid;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final MemberService memberService;
    public AuthController(MemberService memberService) { this.memberService = memberService; }

    /** 현재 인증 여부 */
    private boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    @GetMapping("/login")
    public String loginPage() {
        // ✅ 로그인된 사용자는 홈으로
        if (isAuthenticated()) return "redirect:/";
        return "login";
    }

    @GetMapping("/join")
    public String joinPage(Model model) {
        if (isAuthenticated()) return "redirect:/"; // ✅ 로그인된 사용자는 홈으로
        model.addAttribute("joinForm", new JoinForm());
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) return "join";
        // 추가 검증: 비밀번호 일치 및 길이
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "mismatch", "비밀번호가 서로 일치하지 않습니다.");
            return "join";
        }
        if (form.getPassword().length() < 6) {
            bindingResult.rejectValue("password", "short", "비밀번호는 최소 6자 이상이어야 합니다.");
            return "join";
        }
        try {
            memberService.register(form);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "join";
        }
        return "redirect:/login?joined=1";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("siteTitle", "제주도 가이드");
        return "index";
    }
}
