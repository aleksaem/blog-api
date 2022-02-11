package com.luxoft.blog.mapper;

import com.luxoft.blog.dto.CommentWithPostDto;
import com.luxoft.blog.dto.CommentWithoutPostDto;
import com.luxoft.blog.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = CommentMapper.class)
public interface CommentListMapper {

    CommentListMapper COMMENT_LIST_MAPPER = Mappers.getMapper(CommentListMapper.class);

    List<CommentWithoutPostDto> commentsToCommentWithoutPostDtos(List<Comment> comments);

    List<CommentWithPostDto> commentsToCommentWithPostDtos(List<Comment> comments);
}
