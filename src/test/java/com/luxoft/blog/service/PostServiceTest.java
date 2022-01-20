package com.luxoft.blog.service;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        Post firstPost = Post.builder()
                .postId(1L)
                .postTitle("Films")
                .postContent("Films are soo cool")
                .star(true)
                .build();

        Post secondPost = Post.builder()
                .postId(2L)
                .postTitle("Films")
                .postContent("There are a lot of different kinds of films.")
                .star(false)
                .build();

        List<Post> posts = new ArrayList<>();
        posts.add(firstPost);
        posts.add(secondPost);

        Mockito.when(postService.fetchPostsByTitle("Films")).thenReturn(posts);

    }

    @Test
    void whenValidPostTitle_thenPostShouldBeFound() {
        String title = "Films";
        List<Post> foundPosts = postService.fetchPostsByTitle(title);

        assertEquals(2, foundPosts.size());
    }

    @Test
    void whenInvalidPostTitle_thenPostShouldNotBeFound() {
        String title = "Food";
        List<Post> foundPosts = postService.fetchPostsByTitle(title);

        assertEquals(0, foundPosts.size());
    }

}