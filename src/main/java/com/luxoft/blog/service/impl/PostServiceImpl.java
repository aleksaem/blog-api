package com.luxoft.blog.service.impl;

import com.luxoft.blog.entity.Comment;
import com.luxoft.blog.entity.Post;
import com.luxoft.blog.entity.Tag;
import com.luxoft.blog.repository.CommentRepository;
import com.luxoft.blog.repository.PostRepository;
import com.luxoft.blog.repository.TagRepository;
import com.luxoft.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private CommentRepository commentRepository;

    @Autowired
    public void setPostRepository(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

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
        Optional<Post> postToDelete = postRepository.findById(postId);
        if (postToDelete.isPresent()) {
            if (postToDelete.get().getComments().isEmpty()) {
                postRepository.deleteById(postId);
            } else {
                List<Comment> comments = commentRepository.findAllByPost(postRepository.getById(postId));
                commentRepository.deleteAll(comments);
                postRepository.delete(postToDelete.get());
            }
        } else {
            throw new RuntimeException("Post Cannot Be Found");
        }
    }

    @Override
    public List<Post> fetchPostsByTitle(String postTitle) {

        return postRepository.findByPostTitleIgnoreCase(postTitle);
    }

    @Override
    public void setStarToPostWithId(Long postId) {
        Optional<Post> postFromDB = postRepository.findById(postId);

        if (postFromDB.isPresent()) {
            postFromDB.get().setStar(true);
            postRepository.save(postFromDB.get());
        } else {
            throw new IllegalArgumentException("Post Cannot Be Found");
        }
    }

    @Override
    public void deleteStarFromPostWithId(Long postId) {
        Optional<Post> postFromDB = postRepository.findById(postId);

        if (postFromDB.isPresent()) {
            postFromDB.get().setStar(false);
            postRepository.save(postFromDB.get());
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
