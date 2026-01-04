package com.koreait.jejuguide.controller;

import com.koreait.jejuguide.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class ApiMemberController {

    private final MemberRepository memberRepository;

    public ApiMemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /** 이메일 사용 가능 여부 조회
     *  예) /api/members/check-email?email=foo@bar.com -> {"available":true}
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam("email") String email) {
        boolean exists = memberRepository.findByEmail(email.toLowerCase().trim()).isPresent();
        Map<String, Object> body = new HashMap<>();
        body.put("available", !exists);
        return ResponseEntity.ok(body);
    }
}
