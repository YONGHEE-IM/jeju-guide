package com.koreait.jejuguide.controller;

import com.koreait.jejuguide.service.AccountRecoveryService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RecoveryController {

    private final AccountRecoveryService recoveryService;

    public RecoveryController(AccountRecoveryService recoveryService) {
        this.recoveryService = recoveryService;
    }

    /** 현재 인증 여부 */
    private boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    /** 찾기 화면 (tab = id | pw)  */
    @GetMapping("/account/find")
    public String findPage(@RequestParam(name = "tab", defaultValue = "id") String tab,
                           Model model) {
        // 로그인된 사용자는 굳이 찾기 화면에 올 필요가 없으니 홈으로 돌려보냄 (원치 않으면 이 줄 삭제)
        if (isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("tab", tab);
        // 템플릿에서 null 비교를 안전하게 하기 위한 기본값 세팅
        model.addAttribute("email", null);
        model.addAttribute("tempPassword", null);
        model.addAttribute("hasResult", null);
        model.addAttribute("inputName", null);
        model.addAttribute("emails", null);
        return "account/find";
    }

    /** 아이디(이메일) 찾기 - 이름으로 */
    @PostMapping("/account/find-id")
    public String findId(@RequestParam("name") String name,
                         Model model) {
        List<String> emails = recoveryService.findEmailsByName(name);

        model.addAttribute("tab", "id");
        model.addAttribute("inputName", name);
        model.addAttribute("emails", emails);
        model.addAttribute("hasResult", !emails.isEmpty());

        // pw 탭 변수 기본화(템플릿에서 null-safe)
        model.addAttribute("email", null);
        model.addAttribute("tempPassword", null);

        return "account/find";
    }

    /** 비밀번호 찾기 - 이메일+이름 확인 후 임시 비밀번호 발급 */
    @PostMapping("/account/find-pw")
    public String findPw(@RequestParam("email") String email,
                         @RequestParam("name") String name,
                         Model model) {

        String temp = recoveryService.resetPasswordWithTemp(email, name);

        model.addAttribute("tab", "pw");
        model.addAttribute("email", email);
        model.addAttribute("inputName", name);
        model.addAttribute("tempPassword", temp); // null이면 실패

        // id 탭 변수 기본화
        model.addAttribute("emails", null);
        model.addAttribute("hasResult", null);

        return "account/find";
    }
}
