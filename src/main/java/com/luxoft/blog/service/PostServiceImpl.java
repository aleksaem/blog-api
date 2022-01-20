package com.luxoft.blog.service;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> fetchPostsList() {
        return postRepository.findAll();
    }

    @Override
    public Post updatePost(Long postId, Post post) {
        post.setPostId(postId);
        return postRepository.save(post);
    }

    @Override
    public void deletePostById(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public List<Post> fetchPostsByTitle(String postTitle) {
        return postRepository.findByPostTitleIgnoreCase(postTitle);
    }

    @Override
    public Post setStarToPostWithId(Long postId) {
        Optional<Post> postFromDB = postRepository.findById(postId);

        if (postFromDB.isPresent()) {
            postFromDB.get().setStar(true);
            return postRepository.save(postFromDB.get());
        } else {
            throw new IllegalArgumentException("Post Cannot Be Found");
        }
    }

    @Override
    public Post deleteStarFromPostWithId(Long postId) {
        Optional<Post> postFromDB = postRepository.findById(postId);

        if (postFromDB.isPresent()) {
            postFromDB.get().setStar(false);
            return postRepository.save(postFromDB.get());
        } else {
            throw new IllegalArgumentException("Post Cannot Be Found");
        }
    }

    @Override
    public List<Post> fetchPostsWithStar(boolean star) {
        return postRepository.findByStar(star);
    }

    @Override
    public List<Post> sortPostsByTitle() {
        return postRepository.findAll(Sort.by(Sort.Direction.ASC, "postTitle"));
    }

    @Override
    public Post fetchPostById(Long postId) {
        Optional<Post> foundPost = postRepository.findById(postId);

        if (foundPost.isPresent()) {
            return foundPost.get();
        } else {
            throw new IllegalArgumentException("Post Cannot Be Found");
        }
    }

}
