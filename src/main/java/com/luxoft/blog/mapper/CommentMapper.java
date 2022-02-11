package com.luxoft.blog.mapper;

import com.luxoft.blog.dto.CommentWithPostDto;
import com.luxoft.blog.dto.CommentWithoutPostDto;
import com.luxoft.blog.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

    CommentMapper COMMENT_MAPPER = Mappers.getMapper(CommentMapper.class);

    CommentWithoutPostDto commentToCommentWithoutPostDto(Comment comment);

    CommentWithPostDto commentToCommentWithPostDto(Comment comment);

}
