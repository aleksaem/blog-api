package com.luxoft.blog.service.impl;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.entity.Tag;
import com.luxoft.blog.repository.PostRepository;
import com.luxoft.blog.repository.TagRepository;
import com.luxoft.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagServiceImpl implements TagService {

    TagRepository tagRepository;

    PostRepository postRepository;

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setPostRepository(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Tag saveTag(Tag tag) {
        boolean exist = false;
        List<Tag> allTags = tagRepository.findAll();
        for (Tag existingTag : allTags) {
            if (existingTag.getTagName().equals(tag.getTagName())) {
                exist = true;
                break;
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
            savedTag.setTagId(savedTag.getTagId());
            tagRepository.save(savedTag);
            postFromDB.get().getTags().add(savedTag);
            postFromDB.get().setPostId(postId);
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
    public Set<Tag> getAllTagsToPost(Long postId) {
        Post postFromDB = postRepository.getById(postId);
        return postFromDB.getTags();
    }

    @Override
    public Set<Post> getAllPostsWithTag(Long tagId) {
        Tag tag = tagRepository.getById(tagId);
        return tag.getPosts();
    }

    @Override
    public Set<Post> getAllPostsWithTags(Long firstTagId, Long secondTagId) {
        Tag firstTag = tagRepository.getById(firstTagId);
        Tag secondTag = tagRepository.getById(secondTagId);

        Set<Post> postsWithFirstTag = firstTag.getPosts();
        Set<Post> postsWithSecondTag = secondTag.getPosts();

        Set<Post> result = new HashSet<>();
        result.addAll(postsWithFirstTag);
        result.addAll(postsWithSecondTag);
        return result;
    }
}
