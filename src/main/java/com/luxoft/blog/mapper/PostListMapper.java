package com.luxoft.blog.mapper;

import com.luxoft.blog.dto.FullPostDto;
import com.luxoft.blog.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(uses = PostMapper.class)
public interface PostListMapper {

    PostListMapper POST_LIST_MAPPER = Mappers.getMapper(PostListMapper.class);

    List<FullPostDto> postsToFullPostDtos(List<Post> posts);

    Set<FullPostDto> postsToFullPostDtos(Set<Post> posts);
}
