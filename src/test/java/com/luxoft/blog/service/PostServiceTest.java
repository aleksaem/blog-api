package com.luxoft.blog.service;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
                .postTitle("Music")
                .postContent("There are a lot of different kinds of music.")
                .build();

        Post secondPost = Post.builder()
                .postId(2L)
                .postTitle("Films")
                .postContent("There are a lot of different kinds of films.")
                .build();

        Mockito.when(postService.fetchPostByTitle("Films")).thenReturn(secondPost);

    }

    @Test
    void whenValidPostTitle_thenPostShouldBeFound() {
        String title = "Films";
        Post foundPost = postService.fetchPostByTitle(title);

        assertEquals(title, foundPost.getPostTitle());
    }

    @Test
    void whenInvalidPostTitle_thenPostShouldNotBeFound() {
        String title = "Food";
        Post foundPost = postService.fetchPostByTitle(title);

        assertNull(foundPost);
    }

}