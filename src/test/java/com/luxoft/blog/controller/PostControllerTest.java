package com.luxoft.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxoft.blog.entity.Post;
import com.luxoft.blog.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PostService postService;

    @Test
    @DisplayName("Test save post and expect status OK")
    public void testSavePost() throws Exception {
        Post postToSave = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postToSave)))
                .andExpect(status().isOk());
        verify(postService, times(1)).savePost(postToSave);
    }

    @Test
    @DisplayName("Test save post on incorrect post and expect status BAD REQUEST")
    public void testSavePostWhenPostIsIncorrect() throws Exception {
        Post post = new Post();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(post)))
                .andExpect(status().isBadRequest());
        verify(postService, times(0)).savePost(post);
    }

    @Test
    @DisplayName("Test delete post with correct id and expect status OK")
    public void testDeletePost() throws Exception {
        Post postToDelete = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/posts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postToDelete)))
                .andExpect(status().isOk());
        verify(postService, times(1)).deletePostById(1L);
    }

    @Test
    @DisplayName("Test update post and expect status OK")
    public void testEditPost() throws Exception {
        Post postToEdit = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/posts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postToEdit)))
                .andExpect(status().isOk());
        verify(postService, times(1)).updatePost(1L, postToEdit);
    }

    @Test
    @DisplayName("Test find all posts when list of posts is correct")
    public void testFindAllPosts() throws Exception {
        List<Post> postList = new ArrayList<>();
        Post post_1 = Post.builder()
                .postId(1L)
                .postTitle("Post 1")
                .postContent("Content 1")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        Post post_2 = Post.builder()
                .postId(2L)
                .postTitle("Post 2")
                .postContent("Content 2")
                .star(false)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        Post post_3 = Post.builder()
                .postId(3L)
                .postTitle("Post 3")
                .postContent("Content 3")
                .star(false)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        postList.add(post_1);
        postList.add(post_2);
        postList.add(post_3);

        when(postService.fetchPostsList()).thenReturn(postList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].postId").value(2L))
                .andExpect(jsonPath("$[2].postTitle").value("Post 3"))
                .andExpect(jsonPath("$[0].postContent").value("Content 1"));

        verify(postService, times(1)).fetchPostsList();
    }

    @Test
    @DisplayName("Test find all posts when list of posts is empty")
    public void testFindAllOnEmptyList() throws Exception {
        List<Post> posts = new ArrayList<>();

        when(postService.fetchPostsList()).thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(postService, times(1)).fetchPostsList();
    }

    @Test
    @DisplayName("Test find posts by post title when status is OK")
    public void testFindPostsByTitle() throws Exception {
        Post post_1 = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content 1")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        Post post_3 = Post.builder()
                .postId(3L)
                .postTitle("Post")
                .postContent("Content 3")
                .star(false)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        List<Post> postList = List.of(post_1, post_3);

        when(postService.fetchPostsByTitle("Post")).thenReturn(postList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/posts?postTitle={postTitle}", "Post")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].postId").value(3L))
                .andExpect(jsonPath("$[0].postTitle").value("Post"))
                .andExpect(jsonPath("$[0].postContent").value("Content 1"));

        verify(postService, times(1)).fetchPostsByTitle("Post");
    }

    @Test
    @DisplayName("Test sorting posts by titles")
    public void testSortPostsByTitle() throws Exception {
        Post post_1 = Post.builder()
                .postId(1L)
                .postTitle("Animals")
                .postContent("Content 1")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        Post post_3 = Post.builder()
                .postId(3L)
                .postTitle("Food")
                .postContent("Content 3")
                .star(false)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        List<Post> posts = List.of(post_1, post_3);
        when(postService.sortPostsByTitle()).thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/posts?sort=true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].postTitle").value("Food"))
                .andExpect(jsonPath("$[0].postTitle").value("Animals"))
                .andExpect(jsonPath("$[0].postId").value(1L));
        verify(postService, times(1)).sortPostsByTitle();
    }

    @Test
    @DisplayName("Test setting star to post when post exist")
    public void testSetStarToPost() throws Exception {
        Post post = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content 3")
                .star(false)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/posts/{id}/star", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(post)))
                .andExpect(status().isOk());

        verify(postService, times(1)).setStarToPostWithId(1L);
    }

    @Test
    @DisplayName("Test deleting star from post when post exist")
    public void testDeleteStarFromPost() throws Exception {
        Post post = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content 3")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/posts/{id}/star", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(post)))
                .andExpect(status().isOk());

        verify(postService, times(1)).deleteStarFromPostWithId(1L);
    }

    @Test
    @DisplayName("Test find all posts with star")
    public void testFetchPostsWithStar() throws Exception {
        Post post_1 = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content 1")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        Post post_2 = Post.builder()
                .postId(2L)
                .postTitle("Food")
                .postContent("Content 2")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        List<Post> posts = List.of(post_1, post_2);
        when(postService.fetchPostsWithStar(true)).thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/posts/star")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(posts)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].star").value(true))
                .andExpect(jsonPath("$[1].star").value(true));

        verify(postService, times(1)).fetchPostsWithStar(true);
    }
}