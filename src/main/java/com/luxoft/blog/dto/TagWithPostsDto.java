package com.luxoft.blog.dto;

import lombok.Data;

import java.util.Set;

@Data
public class TagWithPostsDto {

    private Long tagId;

    private String tagName;

    private Set<SimplePostDto> posts;
}
