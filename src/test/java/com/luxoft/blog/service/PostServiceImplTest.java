package com.luxoft.blog.service;


import com.luxoft.blog.entity.Post;
import com.luxoft.blog.repository.PostRepository;
import com.luxoft.blog.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PostServiceImplTest {

    private PostServiceImpl postService;

    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        postService = new PostServiceImpl();
        postService.setPostRepository(postRepository);
    }

    @Test
    void whenValidPostTitle_thenPostShouldBeFound() {
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

        List<Post> posts = List.of(firstPost, secondPost);


        Mockito.when(postService.fetchPostsByTitle("Films")).thenReturn(posts);

        String title = "Films";
        List<Post> foundPosts = postService.fetchPostsByTitle(title);

        assertEquals(2, foundPosts.size());
        assertEquals(1L, foundPosts.get(0).getPostId());
        assertEquals("Films", foundPosts.get(0).getPostTitle());
        assertEquals("Films are soo cool", foundPosts.get(0).getPostContent());
        assertTrue(foundPosts.get(0).isStar());

        assertEquals(2L, foundPosts.get(1).getPostId());
        assertEquals("Films", foundPosts.get(1).getPostTitle());
        assertEquals("There are a lot of different kinds of films.", foundPosts.get(1).getPostContent());
        assertFalse(foundPosts.get(1).isStar());

    }

    @Test
    void whenInvalidPostTitle_thenPostShouldNotBeFound() {
        String title = "Food";
        List<Post> foundPosts = postService.fetchPostsByTitle(title);

        assertEquals(0, foundPosts.size());
        verify(postRepository, times(1)).findByPostTitleIgnoreCase(title);
    }

    @Test
    void testFetchPostsList() {
        Post firstPost = Post.builder()
                .postId(1L)
                .postTitle("Films")
                .postContent("Films are soo cool")
                .star(true)
                .build();

        Post secondPost = Post.builder()
                .postId(2L)
                .postTitle("Cartoons")
                .postContent("There are a lot of different kinds of films.")
                .star(false)
                .build();

        List<Post> posts = List.of(firstPost, secondPost);

        Mockito.when(postRepository.findAll()).thenReturn(posts);

        List<Post> postsFromDB = postService.fetchPostsList();
        assertEquals(2, postsFromDB.size());
        assertEquals("Cartoons", postsFromDB.get(1).getPostTitle());
        assertTrue(postsFromDB.get(0).isStar());
        verify(postRepository, times(1)).findAll();

    }

    @Test
    void testSavePost() {
        Post post1 = Post.builder()
                .postId(1L)
                .postTitle("Films")
                .postContent("Films are soo cool")
                .star(true)
                .build();

        Post post2 = Post.builder()
                .postId(2L)
                .postTitle("Cartoons")
                .postContent("Cartoons are soo cool")
                .star(true)
                .build();

        Post post3 = Post.builder()
                .postId(3L)
                .postTitle("Food")
                .postContent("Food is soo cool")
                .star(true)
                .build();

        postService.savePost(post1);
        postService.savePost(post2);

        Mockito.verify(postRepository, times(2)).save(any(Post.class));
        Mockito.verify(postRepository, never()).save(post3);
        Mockito.verify(postRepository, atLeast(1)).save(any(Post.class));
        Mockito.verify(postRepository, atMost(2)).save(any(Post.class));

    }

    @Test
    void testEditPost() {
        Post originalPost = Post.builder()
                .postId(1L)
                .postTitle("Films")
                .postContent("Films are soo cool")
                .star(true)
                .build();

        postService.updatePost(2L, originalPost);

        Mockito.verify(postRepository, times(1)).save(originalPost);
    }

    @Test
    void testSetStarToPostWithId(){
        Post postWithoutStar = Post.builder()
                .postId(1L)
                .postTitle("Films")
                .postContent("Films are soo cool")
                .star(false)
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(postWithoutStar));

        Post postToEdit = postService.fetchPostById(1L);
        postService.setStarToPostWithId(postToEdit.getPostId());

        assertTrue(postToEdit.isStar());
        verify(postRepository, times(1)).save(postToEdit);
    }

    @Test
    void testDeleteStarFromPostWithId(){
        Post postWithStar = Post.builder()
                .postId(1L)
                .postTitle("Films")
                .postContent("Films are soo cool")
                .star(true)
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(postWithStar));

        Post postToEdit = postService.fetchPostById(1L);
        postService.deleteStarFromPostWithId(postToEdit.getPostId());

        assertFalse(postToEdit.isStar());
        verify(postRepository, times(1)).save(postToEdit);
    }

    @Test
    void testFetchPostsWithStar(){
        Post postWithStar_1 = Post.builder()
                .postId(1L)
                .postTitle("Films")
                .postContent("Films are soo cool")
                .star(true)
                .build();

        Post postWithStar_2 = Post.builder()
                .postId(2L)
                .postTitle("Films")
                .postContent("I like films")
                .star(true)
                .build();

        List<Post> posts = List.of(postWithStar_1, postWithStar_2);
        when(postRepository.findByStar(true)).thenReturn(posts);

        List<Post> postsWithStar = postService.fetchPostsWithStar(true);
        assertNotNull(postsWithStar);
        assertEquals(2, postsWithStar.size());
        assertEquals("Films", postsWithStar.get(0).getPostTitle());
        assertEquals("I like films", postsWithStar.get(1).getPostContent());
        verify(postRepository, times(1)).findByStar(true);
    }

    @Test
    void testSortPostsByTitle(){
        Post post_1 = Post.builder()
                .postId(1L)
                .postTitle("Films")
                .postContent("I like films")
                .star(true)
                .build();

        Post post_2 = Post.builder()
                .postId(2L)
                .postTitle("Animals")
                .postContent("I like animals")
                .star(false)
                .build();

        Post post_3 = Post.builder()
                .postId(3L)
                .postTitle("Cartoons")
                .postContent("I like cartoons")
                .star(true)
                .build();

        List<Post> posts = List.of(post_2, post_3, post_1);
        when(postRepository.findAll(Sort.by(Sort.Direction.ASC, "postTitle"))).thenReturn(posts);

        List<String> postTitles = new ArrayList<>(List.of(post_1.getPostTitle(), post_2.getPostTitle(), post_3.getPostTitle()));
        Collections.sort(postTitles);

        List<Post> sortedPosts = postService.sortPostsByTitle();
        assertNotNull(sortedPosts);
        assertEquals(3, sortedPosts.size());
        assertEquals(postTitles.get(0), sortedPosts.get(0).getPostTitle());
        assertEquals(postTitles.get(1), sortedPosts.get(1).getPostTitle());
        assertEquals(postTitles.get(2), sortedPosts.get(2).getPostTitle());
        verify(postRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "postTitle"));

    }

    @Test
    void testFetchPostById(){
        Post post = Post.builder()
                .postId(1L)
                .postTitle("Films")
                .postContent("I like films")
                .star(true)
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(post));

        Post foundPost = postService.fetchPostById(1L);
        assertNotNull(foundPost);
        assertEquals("Films", foundPost.getPostTitle());
        assertEquals("I like films", foundPost.getPostContent());
        assertTrue(foundPost.isStar());
        verify(postRepository, times(1)).findById(1L);

    }
}