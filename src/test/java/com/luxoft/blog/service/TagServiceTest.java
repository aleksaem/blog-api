package com.luxoft.blog.service;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.entity.Tag;
import com.luxoft.blog.repository.PostRepository;
import com.luxoft.blog.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TagServiceTest {

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private TagService tagService;

    @Test
    @DisplayName("Test save tag is successful")
    public void testSaveTag() {
        Tag tag = Tag.builder()
                .tagId(1L)
                .tagName("Java")
                .posts(new HashSet<>())
                .build();

        when(tagRepository.save(tag)).thenReturn(tag);

        Tag savedTag = tagService.saveTag(tag);
        assertEquals(1L, savedTag.getTagId());
        assertEquals("Java", savedTag.getTagName());
        verify(tagRepository, times(1)).save(tag);
    }

    @Test
    @DisplayName("Test add tag to existent post")
    public void testAddTagToPost() {
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
                .posts(new HashSet<>())
                .build();
        when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(post));
        when(tagRepository.save(tag)).thenReturn(tag);

        Tag savedTag = tagService.addTagToPost(1L, tag);
        verify(tagRepository, times(2)).save(tag);
        assertEquals(1L, savedTag.getTagId());
        assertEquals("Tag", savedTag.getTagName());
        assertTrue(savedTag.getPosts().contains(post));
        assert post != null;
        assertTrue(post.getTags().contains(savedTag));
    }

    @Test
    @DisplayName("Test delete tag that has post inside")
    public void testSaveAndDeleteTag() {
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
                .posts(new HashSet<>())
                .build();
        when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(post));
        when(tagRepository.save(tag)).thenReturn(tag);
        when(tagRepository.getById(1L)).thenReturn(tag);

        Tag savedTag = tagService.addTagToPost(1L, tag);
        assertEquals("Tag", savedTag.getTagName());
        assertTrue(savedTag.getPosts().contains(post));
        assert post != null;
        assertTrue(post.getTags().contains(savedTag));

        tagService.deleteTag(1L);
        assertFalse(post.getTags().contains(savedTag));
        verify(tagRepository, times(1)).delete(savedTag);
    }

    @Test
    @DisplayName("Test delete tag from particular post")
    public void testDeleteTagFromPost() {
        Tag tag = Tag.builder()
                .tagId(1L)
                .tagName("Tag")
                .posts(new HashSet<>())
                .build();

        Post post = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        post.getTags().add(tag);

        when(postRepository.getById(1L)).thenReturn(post);
        when(tagRepository.getById(1L)).thenReturn(tag);

        assertFalse(post.getTags().isEmpty());
        assertTrue(post.getTags().contains(tag));
        tagService.deleteTagFromPost(1L, 1L);
        assertTrue(post.getTags().isEmpty());

    }

    @Test
    @DisplayName("Test get all tags to post")
    public void testGetAllTags(){
        Tag tag1 = Tag.builder()
                .tagId(1L)
                .tagName("Tag 1")
                .posts(new HashSet<>())
                .build();

        Tag tag2 = Tag.builder()
                .tagId(2L)
                .tagName("Tag 2")
                .posts(new HashSet<>())
                .build();

        Tag tag3 = Tag.builder()
                .tagId(3L)
                .tagName("Tag 3")
                .posts(new HashSet<>())
                .build();

        Post post = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();
        post.getTags().add(tag1);
        post.getTags().add(tag2);
        post.getTags().add(tag3);

        when(postRepository.getById(1L)).thenReturn(post);

        Set<Tag> tags = tagService.getAllTagsToPost(1L);
        assertNotNull(tags);
        assertEquals(3, tags.size());
        assertTrue(tags.contains(tag2));
    }

    @Test
    @DisplayName("Test get all posts that contain particular tag")
    public void testGetPostsWithTag(){
        Tag tag = Tag.builder()
                .tagId(1L)
                .tagName("Tag")
                .posts(new HashSet<>())
                .build();

        Post post1 = Post.builder()
                .postId(1L)
                .postTitle("Post 1")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        Post post2 = Post.builder()
                .postId(2L)
                .postTitle("Post 2")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        Post post3 = Post.builder()
                .postId(3L)
                .postTitle("Post 3")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        tag.getPosts().add(post1);
        tag.getPosts().add(post2);
        tag.getPosts().add(post3);
        when(tagRepository.getById(1L)).thenReturn(tag);

        Set<Post> posts = tagService.getAllPostsWithTag(1L);
        assertNotNull(posts);
        assertEquals(3, posts.size());
        assertTrue(posts.contains(post3));
    }

    @Test
    @DisplayName("Test get all posts that contain one of two particular tags or both of them")
    public void testGetPostsWithTags(){
        Tag tag1 = Tag.builder()
                .tagId(1L)
                .tagName("Tag 1")
                .posts(new HashSet<>())
                .build();

        Tag tag2 = Tag.builder()
                .tagId(2L)
                .tagName("Tag 2")
                .posts(new HashSet<>())
                .build();

        Post post1 = Post.builder()
                .postId(1L)
                .postTitle("Post 1")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        Post post2 = Post.builder()
                .postId(2L)
                .postTitle("Post 2")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        Post post3 = Post.builder()
                .postId(3L)
                .postTitle("Post 3")
                .postContent("Content")
                .star(true)
                .comments(new ArrayList<>())
                .tags(new HashSet<>())
                .build();

        tag1.getPosts().add(post1);
        tag1.getPosts().add(post2);
        tag2.getPosts().add(post2);
        tag2.getPosts().add(post3);

        when(tagRepository.getById(1L)).thenReturn(tag1);
        when(tagRepository.getById(2L)).thenReturn(tag2);

        Set<Post> posts = tagService.getAllPostsWithTags(1L, 2L);
        assertNotNull(posts);
        assertEquals(3, posts.size());
        assertTrue(posts.contains(post2));
        assertTrue(posts.contains(post1));
        assertTrue(posts.contains(post3));
    }
}