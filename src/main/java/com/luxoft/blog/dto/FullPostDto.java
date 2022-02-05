package com.luxoft.blog.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class FullPostDto {

    private Long id;

    private String title;

    private String content;

    private boolean star;

    private List<CommentWithoutPostDto> comments;

    private Set<TagWithoutPostsDto> tags;
}
