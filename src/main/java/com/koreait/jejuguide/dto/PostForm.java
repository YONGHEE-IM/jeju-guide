package com.koreait.jejuguide.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostForm {
    @NotBlank @Size(min=2, message="제목은 2자 이상")
    private String title;

    @NotBlank @Size(min=10, message="내용은 10자 이상")
    private String content;

    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
    public String getContent() { return content; }
    public void setContent(String c) { this.content = c; }
}
