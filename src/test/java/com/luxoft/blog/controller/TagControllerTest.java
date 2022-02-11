package com.luxoft.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxoft.blog.entity.Post;
import com.luxoft.blog.entity.Tag;
import com.luxoft.blog.service.TagService;
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
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TagService tagService;

    @DisplayName("Test save tag is successful")
    @Test
    public void testSaveTag() throws Exception {
        Tag tag = Tag.builder()
                .tagId(1L)
                .tagName("Tag")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tag)))
                .andExpect(status().isOk());
        verify(tagService, times(1)).saveTag(tag);
    }

    @DisplayName("Test add tag to post successful")
    @Test
    public void testAddTagToPost() throws Exception {
        Post post = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();
        Tag tag = Tag.builder()
                .tagId(1L)
                .tagName("Tag")
                .posts(Set.of(post))
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1//posts/{id}/tags", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tag)))
                .andExpect(status().isOk());
        verify(tagService, times(1)).addTagToPost(1L, tag);
    }

    @DisplayName("Test delete tag is successful")
    @Test
    public void testDeleteTag() throws Exception {
        Tag tag = Tag.builder()
                .tagId(1L)
                .tagName("Tag")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/tags/{tag_id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tag)))
                .andExpect(status().isOk());
        verify(tagService, times(1)).deleteTag(1L);
    }

    @DisplayName("Test delete tag from particular post successful")
    @Test
    public void testDeleteTagFromPost() throws Exception {
        Post post = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(Set.of(Tag.builder()
                        .tagId(1L)
                        .tagName("Tag")
                        .posts(new HashSet<>())
                        .build()))
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1//posts/{id}/tags/{tag_id}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(post)))
                .andExpect(status().isOk());
        verify(tagService, times(1)).deleteTagFromPost(1L, 1L);
    }

    @DisplayName("Test fetching all existing tags to particular post")
    @Test
    public void testGetAllTagsToPost() throws Exception {
        Post post = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();
        Tag tag1 = Tag.builder()
                .tagId(1L)
                .tagName("Tag")
                .posts(Set.of(post))
                .build();
        Tag tag2 = Tag.builder()
                .tagId(2L)
                .tagName("Tag")
                .posts(Set.of(post))
                .build();
        Set<Tag> tags = Set.of(tag1, tag2);
        when(tagService.getAllTagsToPost(1L)).thenReturn(tags);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1//posts/{id}/tags", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tags)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tagId").value(2L))
                .andExpect(jsonPath("$[1].tagId").value(1L));
        verify(tagService, times(1)).getAllTagsToPost(1L);
    }

    @DisplayName("Test fetching all existing posts with particular tag")
    @Test
    public void testGetAllPostsWithTag() throws Exception {
        Tag tag = Tag.builder()
                .tagId(1L)
                .tagName("Tag")
                .posts(new HashSet<>())
                .build();
        Post post1 = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(Set.of(tag))
                .build();
        Post post2 = Post.builder()
                .postId(2L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(Set.of(tag))
                .build();
        Set<Post> posts = Set.of(post1, post2);
        when(tagService.getAllPostsWithTag(1L)).thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1//posts/tags/{tag_id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(posts)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].postId").value(2L))
                .andExpect(jsonPath("$[1].postId").value(1L));
        verify(tagService, times(1)).getAllPostsWithTag(1L);
    }
}