package com.luxoft.blog.repository;

import com.luxoft.blog.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        Post firstPost = Post.builder()
                .postTitle("Music")
                .postContent("There are a lot of different kinds of music.")
                .star(false)
                .build();

        Post secondPost = Post.builder()
                .postTitle("Food")
                .postContent("Delicious...")
                .star(true)
                .build();

        Post thirdPost = Post.builder()
                .postTitle("Food")
                .postContent("Tasty)")
                .star(false)
                .build();

        entityManager.persist(firstPost);
        entityManager.persist(secondPost);
        entityManager.persist(thirdPost);
    }

    @Test
    public void whenFindByTitle_thenReturnPost() {
        Post post = postRepository.findByPostTitle("Music").get();
        assertEquals(post.getPostContent(), "There are a lot of different kinds of music.");
    }

    @Test
    public void whenFindByWrongTitle_thenReturnEmptyPost() {
        Optional<Post> post = postRepository.findByPostTitle("Sport");
        assertFalse(post.isPresent());
    }

    @Test
    public void fetchAllPostsAndDeleteByIdIsSuccessful() {
        List<Post> posts = postRepository.findAll();

        assertEquals(3, posts.size());
        Post postToDelete = postRepository.getById(2L);
        postRepository.deleteById(postToDelete.getPostId());

        List<Post> updatedPosts = postRepository.findAll();
        assertEquals(2, updatedPosts.size());
    }

    @Test
    public void savePostIsSuccessful() {
        Post newPost = Post.builder()
                .postTitle("Sport")
                .postContent("It`s so hard")
                .build();

        postRepository.save(newPost);
        List<Post> posts = postRepository.findAll();
        assertEquals(4, posts.size());
        assertEquals("Sport", posts.get(3).getPostTitle());
        assertEquals("It`s so hard", posts.get(3).getPostContent());
    }

    @Test
    public void whenFindPostsByTitle_thenReturnListOfPosts() {
        List<Post> postsWithTitleFood = postRepository.findByPostTitleIgnoreCase("Food");

        assertEquals(2, postsWithTitleFood.size());
    }

    @Test
    public void whenFetchPostsByStar_thenReturnListOfPosts() {
        List<Post> postsWithStar = postRepository.findByStar(true);

        assertEquals(1, postsWithStar.size());
    }
}