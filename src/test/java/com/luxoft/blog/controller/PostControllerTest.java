package com.luxoft.blog.controller;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    private Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .postId(1L)
                .postTitle("Music")
                .postContent("There are a lot of different kinds of music.")
                .star(true)
                .build();
    }

    @Test
    void savePostWhenRequestStatusIsOk() throws Exception {
        Post inputPost = Post.builder()
                .postTitle("Music")
                .postContent("There are a lot of different kinds of music.")
                .star(true)
                .build();

        when(postService.savePost(inputPost)).thenReturn(post);

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\t\"postTitle\":\"Music\",\n" +
                                "\t\"postContent\":\"There are a lot of different kinds of music.\",\n" +
                                "\t\"star\":\"true\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    void savePostWhenRequestStatusIsBadRequest() throws Exception {
        Post inputPost = Post.builder()
                .postContent("There are a lot of different kinds of music.")
                .build();

        when(postService.savePost(inputPost)).thenReturn(post);

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\t\"postContent\":\"There are a lot of different kinds of music.\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

}