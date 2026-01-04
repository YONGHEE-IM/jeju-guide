package com.koreait.jejuguide.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentForm {
    @NotBlank @Size(min=1, message="댓글을 입력하세요.")
    private String content;

    public String getContent(){ return content; }
    public void setContent(String c){ this.content = c; }
}
