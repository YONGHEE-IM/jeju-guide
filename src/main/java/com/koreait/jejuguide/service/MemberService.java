package com.koreait.jejuguide.service;

import com.koreait.jejuguide.domain.Member;
import com.koreait.jejuguide.dto.JoinForm;
import com.koreait.jejuguide.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** 기존 컨트롤러(AuthController)의 memberService.register(form)과의 호환용 별칭 */
    @Transactional
    public Member register(JoinForm form) {
        return join(form);
    }

    /** 신규 가입 로직(실 구현) */
    @Transactional
    public Member join(JoinForm form) {
        String email = form.getEmail().toLowerCase().trim();
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        Member m = new Member();
        m.setName(form.getName());
        m.setEmail(email);
        m.setPassword(passwordEncoder.encode(form.getPassword()));
        return memberRepository.save(m);
    }

    /** 비밀번호 변경 */
    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        Member m = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));
        if (!passwordEncoder.matches(currentPassword, m.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("새 비밀번호는 6자 이상이어야 합니다.");
        }
        m.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(m);
    }

    /** 계정 삭제 */
    @Transactional
    public void deleteAccount(String email, String currentPassword) {
        Member m = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));
        if (!passwordEncoder.matches(currentPassword, m.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }
        memberRepository.delete(m);
    }
}
