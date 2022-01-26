package com.luxoft.blog.service;


import com.luxoft.blog.entity.Post;
import com.luxoft.blog.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

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
    }

    @Test
    void testFetchPostsList(){
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

    }

    @Test
    void testSavePost(){
        Post post1 = Post.builder()
                .postTitle("Films")
                .postContent("Films are soo cool")
                .star(true)
                .build();

        Post post2 = Post.builder()
                .postTitle("Cartoons")
                .postContent("Cartoons are soo cool")
                .star(true)
                .build();

        Post post3 = Post.builder()
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
    void testEditPost(){
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
    void testDeletePost(){
        postService.deletePostById(1L);

        Mockito.verify(postRepository, times(1)).deleteById(1L);
    }

//    @Test
//    void testSetStarToPostWithId(){
//
//    }
//
//    @Test
//    void testDeleteStarFromPostWithId(){
//
//    }
//
//    @Test
//    void testFetchPostsWithStar(){
//
//    }
//
//    @Test
//    void testSortPostsByTitle(){
//
//    }
//
//    @Test
//    void testFetchPostById(){
//
//    }
}