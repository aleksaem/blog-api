package com.luxoft.blog.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentWithoutPostDto {

    private Long commentId;

    private LocalDateTime creationDate;

    private String content;
}
