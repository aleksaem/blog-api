package com.luxoft.blog.controller;

import com.luxoft.blog.dto.CommentWithPostDto;
import com.luxoft.blog.dto.SimplePost;
import com.luxoft.blog.entity.Comment;
import com.luxoft.blog.entity.Post;
import com.luxoft.blog.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/v1/posts")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

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

        List<CommentWithPostDto> commentsDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentsDto.add(toCommentWithPostDto(comment));
        }
        return commentsDto;
    }

    @GetMapping("/{id}/comments/{commentId}")
    public CommentWithPostDto fetchComment(@PathVariable("id") Long postId,
                                           @PathVariable("commentId") Long commentId) {
        LOGGER.info("Inside fetchComment of CommentController");
        Comment comment = commentService.fetchComment(postId, commentId);
        return toCommentWithPostDto(comment);
    }

    private CommentWithPostDto toCommentWithPostDto(Comment comment) {
        CommentWithPostDto commentWithPostDto = new CommentWithPostDto();
        commentWithPostDto.setCommentId(comment.getCommentId());
        commentWithPostDto.setContent(comment.getContent());
        commentWithPostDto.setCreationDate(comment.getCreationDate());

        Post post = comment.getPost();
        SimplePost simplePost = new SimplePost();
        simplePost.setPostId(post.getPostId());
        simplePost.setPostTitle(post.getPostTitle());
        simplePost.setPostContent(post.getPostContent());
        simplePost.setStar(post.isStar());

        commentWithPostDto.setSimplePost(simplePost);

        return commentWithPostDto;
    }
}
