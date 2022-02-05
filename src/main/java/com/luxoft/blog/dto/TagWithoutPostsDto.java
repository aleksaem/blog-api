package com.luxoft.blog.dto;

import lombok.Data;

@Data
public class TagWithoutPostsDto {

    private Long tagId;

    private String tagName;
}
