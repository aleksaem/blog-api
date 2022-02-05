package com.luxoft.blog.service;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.entity.Tag;

import java.util.Set;

public interface TagService {

    Tag saveTag(Tag tag);

    Tag addTagToPost(Long postId, Tag tag);

    void deleteTag(Long tagId);

    void deleteTagFromPost(Long postId, Long tagId);

    Set<Tag> getAllTags(Long postId);

    Set<Post> getAllPostsWithTag(Long tagId);

    Set<Post> getAllPostsWithTags(Set<Tag> tags);
}
