package com.luxoft.blog.controller;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.error.PostNotFoundException;
import com.luxoft.blog.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @PostMapping("/api/v1/posts")
    public Post savePost(@RequestBody @Valid Post post) {
        LOGGER.info("Inside savePost of PostController");
        return postService.savePost(post);
    }

    @GetMapping("/api/v1/posts")
    public List<Post> fetchPostsList() {
        LOGGER.info("Inside fetchPostsList of PostController");
        return postService.fetchPostsList();
    }

    @GetMapping("/api/v1/posts/{id}")
    public Post fetchPostById(@PathVariable("id") Long postId) throws PostNotFoundException {
        LOGGER.info("Inside fetchPostById of PostController");
        return postService.fetchPostById(postId);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Post updatePost(@PathVariable("id") Long postId,
                           @RequestBody Post post) {
        LOGGER.info("Inside updatePost of PostController");
        return postService.updatePost(postId, post);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public String deletePostById(@PathVariable("id") Long postId){
        LOGGER.info("Inside deletePostById of PostController");
        postService.deletePostById(postId);
        return "Post with id " + postId + " was deleted successfully!";
    }

}
