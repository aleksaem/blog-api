package com.luxoft.blog.service;

import com.luxoft.blog.entity.Post;

import java.util.List;

public interface PostService {

    public Post savePost(Post post);

    public List<Post> fetchPostsList();

    public Post updatePost(Long postId, Post post);

    public void deletePostById(Long postId);

    public List<Post> fetchPostsByTitle(String postTitle);

    public Post setStarToPostWithId(Long postId);

    public Post deleteStarFromPostWithId(Long postId);

    public List<Post> fetchPostsWithStar(boolean star);

    public List<Post> sortPostsByTitle();

    public Post fetchPostById(Long postId);
}
