package com.koreait.jejuguide.config;

import com.koreait.jejuguide.domain.Member;
import com.koreait.jejuguide.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/** 앱 시작 시 관리자 계정 보장 (email=admin@naver.com, name=admin, pw=admin1234) */
@Configuration
public class AdminDataInitializer {

    @Bean
    CommandLineRunner initAdmin(MemberRepository memberRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@naver.com";
            Optional<Member> existing = memberRepository.findByEmail(adminEmail);
            if (existing.isEmpty()) {
                Member admin = new Member();
                admin.setName("admin");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin1234"));
                memberRepository.save(admin);
            }
        };
    }
}
