package com.luxoft.blog.service;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.entity.Tag;
import com.luxoft.blog.repository.PostRepository;
import com.luxoft.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    PostRepository postRepository;

    @Override
    public Tag saveTag(Tag tag) {
        boolean exist = false;
        List<Tag> allTags = tagRepository.findAll();
        for (Tag existingTag : allTags) {
            if (existingTag.getTagName().equals(tag.getTagName())) {
                exist = true;
            }
        }

        if (!exist) {
            return tagRepository.save(tag);
        } else {
            return tag;
        }
    }

    @Override
    public Tag addTagToPost(Long postId, Tag tag) {
        Tag savedTag = saveTag(tag);
        Optional<Post> postFromDB = postRepository.findById(postId);
        if(postFromDB.isPresent()) {
            savedTag.getPosts().add(postFromDB.get());
            tagRepository.save(savedTag);
            postFromDB.get().getTags().add(savedTag);
            postRepository.save(postFromDB.get());
            return savedTag;
        } else{
            throw new RuntimeException("Post cannot be found");
        }
    }

    @Override
    public void deleteTag(Long tagId) {
        Tag tagToDelete = tagRepository.getById(tagId);
        Set<Post> postsWithTag = tagToDelete.getPosts();
        for (Post post : postsWithTag) {
            post.getTags().remove(tagToDelete);
        }

        tagRepository.delete(tagToDelete);
    }

    @Override
    public void deleteTagFromPost(Long postId, Long tagId) {
        Post postFromDB = postRepository.getById(postId);
        Tag tagToDelete = tagRepository.getById(tagId);

        postFromDB.getTags().remove(tagToDelete);
        postRepository.save(postFromDB);
    }

    @Override
    public Set<Tag> getAllTags(Long postId) {
        Post postFromDB = postRepository.getById(postId);
        return postFromDB.getTags();
    }

    @Override
    public Set<Post> getAllPostsWithTag(Long tagId) {
        Tag tag = tagRepository.getById(tagId);
        return tag.getPosts();
    }

    @Override
    public Set<Post> getAllPostsWithTags(Set<Tag> tags) {
        return null;
    }
}
