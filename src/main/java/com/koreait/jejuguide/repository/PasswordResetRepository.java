package com.koreait.jejuguide.repository;

import com.koreait.jejuguide.domain.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByToken(String token);
    void deleteByToken(String token);
    void deleteAllByMemberId(Long memberId);
}