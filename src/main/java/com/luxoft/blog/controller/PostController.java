package com.luxoft.blog.controller;

import com.luxoft.blog.dto.FullPostDto;
import com.luxoft.blog.entity.Post;
import com.luxoft.blog.mapper.PostListMapper;
import com.luxoft.blog.mapper.PostMapper;
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

    private PostService postService;

    @Autowired
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @PostMapping
    public void savePost(@RequestBody @Valid Post post) {
        LOGGER.info("Inside savePost of PostController");
        postService.savePost(post);
    }

    @GetMapping
    public List<FullPostDto> fetchPostsList(@RequestParam(value = "postTitle", required = false) String postTitle,
                                            @RequestParam(value = "sort", required = false) boolean sort) {
        LOGGER.info("Inside fetchPostsList of PostController");
        if (postTitle != null) {
            List<Post> posts = postService.fetchPostsByTitle(postTitle);
            return PostListMapper.POST_LIST_MAPPER.postsToFullPostDtos(posts);
        } else if (sort) {
            List<Post> posts = postService.sortPostsByTitle();
            return PostListMapper.POST_LIST_MAPPER.postsToFullPostDtos(posts);
        } else {
            List<Post> posts = postService.fetchPostsList();
            return PostListMapper.POST_LIST_MAPPER.postsToFullPostDtos(posts);
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
    public List<FullPostDto> fetchPostsWithStar() {
        LOGGER.info("Inside fetchPostsWithStar of PostController");
        List<Post> posts = postService.fetchPostsWithStar(true);
        return PostListMapper.POST_LIST_MAPPER.postsToFullPostDtos(posts);
    }

    @GetMapping("/{id}")
    public FullPostDto fetchPostById(@PathVariable("id") Long postId) {
        LOGGER.info("Inside fetchPostById of PostController");
        Post post = postService.fetchPostById(postId);
        return PostMapper.POST_MAPPER.postToFullPostDto(post);
    }
}
