package com.koreait.jejuguide.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

        // 하위호환용 래퍼: 저장된 값이 BCrypt면 BCrypt로 검증, 아니면 평문 비교 허용
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                // 새로 저장/변경하는 비번은 항상 BCrypt로 저장
                return bcrypt.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String stored) {
                if (stored == null) return false;
                // BCrypt 해시 패턴이면 BCrypt로 검증
                if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
                    return bcrypt.matches(rawPassword, stored);
                }
                // 레거시(평문) 지원: DB에 평문이 있던 계정도 로그인 가능하게
                return stored.equals(rawPassword.toString());
            }
        };
    }
}