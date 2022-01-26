package com.luxoft.blog.service;

import com.luxoft.blog.dto.CommentWithPostDto;
import com.luxoft.blog.entity.Comment;

import java.util.List;

public interface CommentService {

    CommentWithPostDto saveComment(Long postId, Comment comment);

    List<CommentWithPostDto> fetchAllComments(Long postId);

    CommentWithPostDto fetchComment(Long postId, Long commentId);
}
