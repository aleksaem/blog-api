package com.luxoft.blog.mapper;

import com.luxoft.blog.dto.FullPostDto;
import com.luxoft.blog.dto.SimplePostDto;
import com.luxoft.blog.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CommentListMapper.class, TagListMapper.class})
public interface PostMapper {

    PostMapper POST_MAPPER = Mappers.getMapper(PostMapper.class);

    SimplePostDto postToSimplePostDto(Post post);

    Post simplePostDtoToPost(SimplePostDto simplePostDto);

    FullPostDto postToFullPostDto(Post post);

    Post fullPostDtoToPost(FullPostDto fullPostDto);
}
