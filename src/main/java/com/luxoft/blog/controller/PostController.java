package com.luxoft.blog.controller;

import com.luxoft.blog.dto.CommentWithPostDto;
import com.luxoft.blog.dto.CommentWithoutPostDto;
import com.luxoft.blog.dto.PostWithCommentsDto;
import com.luxoft.blog.entity.Comment;
import com.luxoft.blog.entity.Post;
import com.luxoft.blog.service.CommentService;
import com.luxoft.blog.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @PostMapping
    public void savePost(@RequestBody @Valid Post post) {
        LOGGER.info("Inside savePost of PostController");
        postService.savePost(post);
    }

    @GetMapping
    public List<PostWithCommentsDto> fetchPostsList(@RequestParam(value = "postTitle", required = false) String postTitle,
                                                    @RequestParam(value = "sort", required = false) boolean sort) {
        LOGGER.info("Inside fetchPostsList of PostController");
        if (postTitle != null) {
            List<Post> posts = postService.fetchPostsByTitle(postTitle);
            return getPostsWithComments(posts);
        } else if (sort) {
            List<Post> posts = postService.sortPostsByTitle();
            return getPostsWithComments(posts);
        } else {
            List<Post> posts = postService.fetchPostsList();
            return getPostsWithComments(posts);
        }
    }

    @PutMapping("/{id}")
    public void updatePost(@PathVariable("id") Long postId,
                           @RequestBody Post post) {
        LOGGER.info("Inside updatePost of PostController");
        postService.updatePost(postId, post);
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable("id") Long postId) {
        LOGGER.info("Inside deletePostById of PostController");
        postService.deletePostById(postId);
    }

    @PutMapping("/{id}/star")
    public void setStarToPostWithId(@PathVariable("id") Long postId) {
        LOGGER.info("Inside setStarToPostWithId of PostController");
        postService.setStarToPostWithId(postId);
    }

    @DeleteMapping("/{id}/star")
    public void deleteStarFromPostWithId(@PathVariable("id") Long postId) {
        LOGGER.info("Inside deleteStarFromPostWithId of PostController");
        postService.deleteStarFromPostWithId(postId);
    }

    @GetMapping("/star")
    public List<PostWithCommentsDto> fetchPostsWithStar() {
        LOGGER.info("Inside fetchPostsWithStar of PostController");
        List<Post> posts = postService.fetchPostsWithStar(true);
        return getPostsWithComments(posts);
    }

    @GetMapping("/{id}")
    public PostWithCommentsDto fetchPostById(@PathVariable("id") Long postId) {
        LOGGER.info("Inside fetchPostById of PostController");
        Post post = postService.fetchPostById(postId);
        return getPostWithComments(post);
    }

    @PostMapping("/{id}/comments")
    public void saveComment(@PathVariable("id") Long postId,
                            @RequestBody @Valid Comment comment) {
        LOGGER.info("Inside saveComment of PostController");
        commentService.saveComment(postId, comment);
    }

    @GetMapping("/{id}/comments")
    public List<CommentWithPostDto> fetchAllComments(@PathVariable("id") Long postId) {
        LOGGER.info("Inside fetchAllComments of PostController");
        return commentService.fetchAllComments(postId);
    }

    @GetMapping("/{id}/comments/{commentId}")
    public CommentWithPostDto fetchComment(@PathVariable("id") Long postId,
                                           @PathVariable("commentId") Long commentId) {
        LOGGER.info("Inside fetchComment of PostController");
        return commentService.fetchComment(postId, commentId);
    }

    private List<PostWithCommentsDto> getPostsWithComments(List<Post> posts) {
        List<PostWithCommentsDto> postsWithComments = new ArrayList<>();
        for (Post post : posts) {
            List<Comment> comments = post.getComments();
            List<CommentWithoutPostDto> commentWithoutPostDtos = new ArrayList<>();

            if (!comments.isEmpty()) {
                for (Comment comment : comments) {
                    CommentWithoutPostDto commentWithoutPostDto = new CommentWithoutPostDto();
                    commentWithoutPostDto.setCommentId(comment.getCommentId());
                    commentWithoutPostDto.setContent(comment.getContent());
                    commentWithoutPostDto.setCreationDate(comment.getCreationDate());

                    commentWithoutPostDtos.add(commentWithoutPostDto);
                }
            }

            PostWithCommentsDto postWithCommentsDto = new PostWithCommentsDto();
            postWithCommentsDto.setId(post.getPostId());
            postWithCommentsDto.setTitle(post.getPostTitle());
            postWithCommentsDto.setContent(post.getPostContent());
            postWithCommentsDto.setStar(post.isStar());
            postWithCommentsDto.setComments(commentWithoutPostDtos);

            postsWithComments.add(postWithCommentsDto);
        }
        return postsWithComments;
    }

    private PostWithCommentsDto getPostWithComments(Post post) {
        PostWithCommentsDto postWithComments = new PostWithCommentsDto();

        List<Comment> comments = post.getComments();
        List<CommentWithoutPostDto> commentWithoutPostDtos = new ArrayList<>();

        if (!comments.isEmpty()) {
            for (Comment comment : comments) {
                CommentWithoutPostDto commentWithoutPostDto = new CommentWithoutPostDto();
                commentWithoutPostDto.setCommentId(comment.getCommentId());
                commentWithoutPostDto.setContent(comment.getContent());
                commentWithoutPostDto.setCreationDate(comment.getCreationDate());

                commentWithoutPostDtos.add(commentWithoutPostDto);
            }
        }

        postWithComments.setId(post.getPostId());
        postWithComments.setTitle(post.getPostTitle());
        postWithComments.setContent(post.getPostContent());
        postWithComments.setStar(post.isStar());
        postWithComments.setComments(commentWithoutPostDtos);


        return postWithComments;
    }
}
