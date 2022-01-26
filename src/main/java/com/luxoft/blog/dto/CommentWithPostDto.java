package com.luxoft.blog.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentWithPostDto {

    private Long commentId;

    private LocalDateTime creationDate;

    private String content;

    private PostWithoutCommentDto postWithoutCommentDto;
}
