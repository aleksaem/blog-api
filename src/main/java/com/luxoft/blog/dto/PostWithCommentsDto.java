package com.luxoft.blog.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostWithCommentsDto {

    private Long id;

    private String title;

    private String content;

    private boolean star;

    private List<CommentWithoutPostDto> comments;
}
