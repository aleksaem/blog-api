package com.luxoft.blog.service;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.error.PostNotFoundException;

import java.util.List;

public interface PostService {

    public Post savePost(Post post);

    public List<Post> fetchPostsList();

    public Post updatePost(Long postId, Post post);

    public Post fetchPostById(Long postId) throws PostNotFoundException;

    public void deletePostById(Long postId);
}
