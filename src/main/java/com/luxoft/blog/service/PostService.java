package com.luxoft.blog.service;

import com.luxoft.blog.entity.Post;

import java.util.List;

public interface PostService {

    public Post savePost(Post post);

    public List<Post> fetchPostsList();

    public Post updatePost(Long postId, Post post);

    public void deletePostById(Long postId);

    public Post fetchPostByTitle(String postTitle);
}
