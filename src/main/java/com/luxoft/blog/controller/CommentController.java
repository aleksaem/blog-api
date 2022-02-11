package com.luxoft.blog.controller;

import com.luxoft.blog.dto.CommentWithPostDto;
import com.luxoft.blog.entity.Comment;
import com.luxoft.blog.mapper.CommentListMapper;
import com.luxoft.blog.mapper.CommentMapper;
import com.luxoft.blog.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/v1/posts")
@RestController
public class CommentController {

    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @PostMapping("/{id}/comments")
    public void saveComment(@PathVariable("id") Long postId,
                            @RequestBody @Valid Comment comment) {
        LOGGER.info("Inside saveComment of CommentController");
        commentService.saveComment(postId, comment);
    }

    @GetMapping("/{id}/comments")
    public List<CommentWithPostDto> fetchAllComments(@PathVariable("id") Long postId) {
        LOGGER.info("Inside fetchAllComments of CommentController");
        List<Comment> comments = commentService.fetchAllComments(postId);

        return CommentListMapper.COMMENT_LIST_MAPPER.commentsToCommentWithPostDtos(comments);
    }

    @GetMapping("/{id}/comments/{commentId}")
    public CommentWithPostDto fetchComment(@PathVariable("id") Long postId,
                                           @PathVariable("commentId") Long commentId) {
        LOGGER.info("Inside fetchComment of CommentController");
        Comment comment = commentService.fetchComment(postId, commentId);
        return CommentMapper.COMMENT_MAPPER.commentToCommentWithPostDto(comment);
    }
}
