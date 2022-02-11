package com.luxoft.blog.service;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.entity.Tag;

import java.util.List;

public interface PostService {

    Post savePost(Post post);

    List<Post> fetchPostsList();

    Post updatePost(Long postId, Post post);

    void deletePostById(Long postId);

    List<Post> fetchPostsByTitle(String postTitle);

    void setStarToPostWithId(Long postId);

    void deleteStarFromPostWithId(Long postId);

    List<Post> fetchPostsWithStar(boolean star);

    List<Post> sortPostsByTitle();

    Post fetchPostById(Long postId);
}
