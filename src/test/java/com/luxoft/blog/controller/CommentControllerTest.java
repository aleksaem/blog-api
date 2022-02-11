package com.luxoft.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxoft.blog.entity.Comment;
import com.luxoft.blog.entity.Post;
import com.luxoft.blog.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CommentService commentService;

    @DisplayName("Test save comment to post is successful")
    @Test
    public void testSaveComment() throws Exception {
        Post postWithComments = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();
        Comment comment = Comment.builder()
                .commentId(1L)
                .content("Comment")
                .post(postWithComments)
                .creationDate(LocalDateTime.now())
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/posts/{id}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(comment)))
                .andExpect(status().isOk());

        verify(commentService, times(1)).saveComment(1L, comment);
    }

    @DisplayName("Test fetch all existing comments to post")
    @Test
    public void testFetchAllCommentsToPost() throws Exception {
        Post postWithComments = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();
        Comment comment_1 = Comment.builder()
                .commentId(1L)
                .content("Comment 1")
                .post(postWithComments)
                .creationDate(LocalDateTime.now())
                .build();
        Comment comment_2 = Comment.builder()
                .commentId(2L)
                .content("Comment 2")
                .post(postWithComments)
                .creationDate(LocalDateTime.now())
                .build();
        List<Comment> comments = List.of(comment_1, comment_2);
        when(commentService.fetchAllComments(1L)).thenReturn(comments);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/posts/{id}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(comments)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].commentId").value(1L))
                .andExpect(jsonPath("$[1].commentId").value(2L))
                .andExpect(jsonPath("$[1].content").value("Comment 2"));
        verify(commentService, times(1)).fetchAllComments(1L);
    }

    @DisplayName("Test fetch particular comment to post")
    @Test
    public void testFetchCommentToPost() throws Exception {
        Post postWithComments = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();
        Comment comment = Comment.builder()
                .commentId(13L)
                .content("Comment")
                .post(postWithComments)
                .creationDate(LocalDateTime.now())
                .build();
        when(commentService.fetchComment(1L, 13L)).thenReturn(comment);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/posts/{id}/comments/{commentId}", 1L, 13L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(13L))
                .andExpect(jsonPath("$.content").value("Comment"));
        verify(commentService, times(1)).fetchComment(1L, 13L);
    }
}
