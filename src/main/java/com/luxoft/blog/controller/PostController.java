package com.luxoft.blog.controller;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    @Autowired
    private PostService postService;

    private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @PostMapping
    public Post savePost(@RequestBody @Valid Post post) {
        LOGGER.info("Inside savePost of PostController");
        return postService.savePost(post);
    }

    @GetMapping
    public List<Post> fetchPostsList(@RequestParam(value = "postTitle", required = false) String postTitle,
                                     @RequestParam(value = "sort", required = false) boolean sort) {
        LOGGER.info("Inside fetchPostsList of PostController");
        if (postTitle != null) {
            return postService.fetchPostsByTitle(postTitle);
        } else if (sort) {
            return postService.sortPostsByTitle();
        } else {
            return postService.fetchPostsList();
        }
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable("id") Long postId,
                           @RequestBody Post post) {
        LOGGER.info("Inside updatePost of PostController");
        return postService.updatePost(postId, post);
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable("id") Long postId) {
        LOGGER.info("Inside deletePostById of PostController");
        postService.deletePostById(postId);
    }

    @PutMapping("/{id}/star")
    public Post setStarToPostWithId(@PathVariable("id") Long postId) {
        LOGGER.info("Inside setStarToPostWithId of PostController");
        return postService.setStarToPostWithId(postId);
    }

    @DeleteMapping("/{id}/star")
    public Post deleteStarFromPostWithId(@PathVariable("id") Long postId) {
        LOGGER.info("Inside deleteStarFromPostWithId of PostController");
        return postService.deleteStarFromPostWithId(postId);
    }

    @GetMapping("/star")
    public List<Post> fetchPostsWithStar() {
        LOGGER.info("Inside fetchPostsWithStar of PostController");
        return postService.fetchPostsWithStar(true);
    }

    @GetMapping("/{id}")
    public Post fetchPostById(@PathVariable("id") Long postId) {
        LOGGER.info("Inside fetchPostById of PostController");
        return postService.fetchPostById(postId);
    }

}
