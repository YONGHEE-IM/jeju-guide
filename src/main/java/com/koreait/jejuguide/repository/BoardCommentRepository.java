package com.koreait.jejuguide.repository;

import com.koreait.jejuguide.domain.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {}
