package com.luxoft.blog.service;

import com.luxoft.blog.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment saveComment(Long postId, Comment comment);

    List<Comment> fetchAllComments(Long postId);

    Comment fetchComment(Long postId, Long commentId);
}
