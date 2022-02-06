package com.luxoft.blog.controller;

import com.luxoft.blog.entity.Post;
import com.luxoft.blog.entity.Tag;
import com.luxoft.blog.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

@RequestMapping("/api/v1")
@RestController
public class TagController {

    @Autowired
    TagService tagService;

    private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @PostMapping("/tags")
    public void saveTag(@RequestBody @Valid Tag tag) {
        LOGGER.info("Inside saveTag of TagController");
        tagService.saveTag(tag);
    }

    @PostMapping("/posts/{id}/tags")
    public void addTagToPost(@PathVariable("id") Long postId,
                             @RequestBody @Valid Tag tag) {
        LOGGER.info("Inside addTagToPost of TagController");
        tagService.addTagToPost(postId, tag);
    }

    @DeleteMapping("/tags/{tag_id}")
    public void deleteTag(@PathVariable("tag_id") Long tagId) {
        LOGGER.info("Inside deleteTag of TagController");
        tagService.deleteTag(tagId);
    }

    @DeleteMapping("/posts/{id}/tags/{tag_id}")
    public void deleteTagFromPost(@PathVariable("id") Long postId,
                                  @PathVariable("tag_id") Long tagId) {
        LOGGER.info("Inside deleteTagFromPost of TagController");
        tagService.deleteTagFromPost(postId, tagId);
    }

    @GetMapping("/posts/{id}/tags")
    public Set<Tag> getAllTagsToPost(@PathVariable("id") Long postId) {
        LOGGER.info("Inside getAllTagsToPost of TagController");
        return tagService.getAllTagsToPost(postId);
    }

    @GetMapping("/posts/tags/{tagId}")
    public Set<Post> getAllPostsWithTag(@PathVariable("tagId") Long tagId) {
        LOGGER.info("Inside getAllPostsWithTag of TagController");
        return tagService.getAllPostsWithTag(tagId);
    }
}
