package com.luxoft.blog.dto;

import lombok.Data;

@Data
public class SimplePostDto {

    private Long postId;

    private String postTitle;

    private String postContent;

    private boolean star;
}
