package com.luxoft.blog.service;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.error.PostNotFoundException;
import com.luxoft.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
        Post postFromDB = postRepository.findById(postId).get();

        if (Objects.nonNull(post.getPostTitle()) &&
                !"".equalsIgnoreCase(post.getPostTitle())){
            postFromDB.setPostTitle(post.getPostTitle());
        }

        if (Objects.nonNull(post.getPostContent()) &&
                !"".equalsIgnoreCase(post.getPostContent())){
            postFromDB.setPostContent(post.getPostContent());
        }

        return postRepository.save(postFromDB);
    }

    @Override
    public Post fetchPostById(Long postId) throws PostNotFoundException {
        Optional<Post> post = postRepository.findById(postId);

        if (!post.isPresent()) {
            throw new PostNotFoundException("Post Not Available!");
        } else {
            return post.get();
        }
    }

    @Override
    public void deletePostById(Long postId) {
        postRepository.deleteById(postId);
    }


}
