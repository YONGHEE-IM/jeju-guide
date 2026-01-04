package com.koreait.jejuguide.controller.admin;

import com.koreait.jejuguide.domain.Member;
import com.koreait.jejuguide.repository.MemberRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MemberRepository memberRepository;

    public AdminController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 관리자 이메일 확인
    private boolean isAdmin(User principal) {
        return principal != null && "admin@naver.com".equalsIgnoreCase(principal.getUsername());
    }

    @GetMapping
    public String dashboard(@AuthenticationPrincipal User principal, Model model) {
        if (!isAdmin(principal)) return "redirect:/login";
        long totalUsers = memberRepository.count();
        model.addAttribute("totalUsers", totalUsers);
        return "admin/dashboard";
    }

    @GetMapping("/members")
    public String members(@AuthenticationPrincipal User principal, Model model) {
        if (!isAdmin(principal)) return "redirect:/login";
        List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);
        model.addAttribute("adminEmail", "admin@naver.com");
        return "admin/members";
    }

    @PostMapping("/members/{id}/delete")
    @Transactional
    public String deleteMember(@PathVariable("id") Long id,
                               @AuthenticationPrincipal User principal) {
        if (!isAdmin(principal)) return "redirect:/login";
        Member m = memberRepository.findById(id).orElse(null);
        if (m != null && !"admin@naver.com".equalsIgnoreCase(m.getEmail())) {
            memberRepository.deleteById(id);
        }
        return "redirect:/admin/members?deleted=1";
    }
}
