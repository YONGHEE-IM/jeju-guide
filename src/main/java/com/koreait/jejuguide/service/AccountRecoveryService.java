package com.koreait.jejuguide.service;

import com.koreait.jejuguide.domain.Member;

import com.koreait.jejuguide.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
public class AccountRecoveryService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random = new SecureRandom();

    public AccountRecoveryService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** 이름으로 이메일(아이디) 목록 조회 */
    @Transactional(readOnly = true)
    public List<String> findEmailsByName(String name) {
        return memberRepository.findByNameIgnoreCase(name).stream()
                .map(Member::getEmail)
                .toList();
    }

    /** 이메일+이름 확인 → 임시 비밀번호 발급/저장 후 반환 (실패시 null) */
    @Transactional
    public String resetPasswordWithTemp(String email, String name) {
        return memberRepository.findByEmail(email.toLowerCase().trim())
                .filter(m -> m.getName().equalsIgnoreCase(name)).map(m -> {
                    String temp = generateTempPassword();
                    m.setPassword(passwordEncoder.encode(temp));
                    memberRepository.save(m);
                    // TODO: 실제 서비스에서는 여기서 메일 발송(템플릿) 수행
                    return temp;
                })
                .orElse(null);
    }

    /** 임시 비밀번호 생성(영문/숫자 10자리) */
    private String generateTempPassword() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int idx = random.nextInt(chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }
}

