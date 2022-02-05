package com.luxoft.blog.controller;

import com.luxoft.blog.dto.CommentWithoutPostDto;
import com.luxoft.blog.dto.FullPostDto;
import com.luxoft.blog.dto.TagWithoutPostsDto;
import com.luxoft.blog.entity.Comment;
import com.luxoft.blog.entity.Post;
import com.luxoft.blog.entity.Tag;
import com.luxoft.blog.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    @Autowired
    private PostService postService;

    private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @PostMapping
    public void savePost(@RequestBody @Valid Post post) {
        LOGGER.info("Inside savePost of PostController");
        postService.savePost(post);
    }

    @GetMapping
    public List<FullPostDto> fetchPostsList(@RequestParam(value = "postTitle", required = false) String postTitle,
                                            @RequestParam(value = "sort", required = false) boolean sort) {
        LOGGER.info("Inside fetchPostsList of PostController");
        if (postTitle != null) {
            List<Post> posts = postService.fetchPostsByTitle(postTitle);
            return getFullPosts(posts);
        } else if (sort) {
            List<Post> posts = postService.sortPostsByTitle();
            return getFullPosts(posts);
        } else {
            List<Post> posts = postService.fetchPostsList();
            return getFullPosts(posts);
        }
    }

    @PutMapping("/{id}")
    public void updatePost(@PathVariable("id") Long postId,
                           @RequestBody Post post) {
        LOGGER.info("Inside updatePost of PostController");
        postService.updatePost(postId, post);
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable("id") Long postId) {
        LOGGER.info("Inside deletePostById of PostController");
        postService.deletePostById(postId);
    }

    @PutMapping("/{id}/star")
    public void setStarToPostWithId(@PathVariable("id") Long postId) {
        LOGGER.info("Inside setStarToPostWithId of PostController");
        postService.setStarToPostWithId(postId);
    }

    @DeleteMapping("/{id}/star")
    public void deleteStarFromPostWithId(@PathVariable("id") Long postId) {
        LOGGER.info("Inside deleteStarFromPostWithId of PostController");
        postService.deleteStarFromPostWithId(postId);
    }

    @GetMapping("/star")
    public List<FullPostDto> fetchPostsWithStar() {
        LOGGER.info("Inside fetchPostsWithStar of PostController");
        List<Post> posts = postService.fetchPostsWithStar(true);
        return getFullPosts(posts);
    }

    @GetMapping("/{id}")
    public FullPostDto fetchPostById(@PathVariable("id") Long postId) {
        LOGGER.info("Inside fetchPostById of PostController");
        Post post = postService.fetchPostById(postId);
        return getFullPost(post);
    }

    private List<FullPostDto> getFullPosts(List<Post> posts) {
        List<FullPostDto> postsWithComments = new ArrayList<>();
        for (Post post : posts) {
            List<Comment> comments = post.getComments();
            Set<Tag> tags = post.getTags();
            List<CommentWithoutPostDto> commentWithoutPostDtos = new ArrayList<>();
            Set<TagWithoutPostsDto> tagWithoutPostsDtos = new HashSet<>();

            if (!comments.isEmpty()) {
                for (Comment comment : comments) {
                    CommentWithoutPostDto commentWithoutPostDto = toCommentWithoutPostDto(comment);

                    commentWithoutPostDtos.add(commentWithoutPostDto);
                }
            }

            if (!tags.isEmpty()) {
                for (Tag tag : tags) {
                    TagWithoutPostsDto tagWithoutPostsDto = toTagWithoutPostsDto(tag);

                    tagWithoutPostsDtos.add(tagWithoutPostsDto);
                }
            }

            FullPostDto fullPostDto = toPostWithCommentsDto(post, commentWithoutPostDtos, tagWithoutPostsDtos);

            postsWithComments.add(fullPostDto);
        }
        return postsWithComments;
    }

    private FullPostDto getFullPost(Post post) {
        FullPostDto postWithComments;

        List<Comment> comments = post.getComments();
        Set<Tag> tags = post.getTags();
        List<CommentWithoutPostDto> commentWithoutPostDtos = new ArrayList<>();
        Set<TagWithoutPostsDto> tagWithoutPostsDtos = new HashSet<>();

        if (!comments.isEmpty()) {
            for (Comment comment : comments) {
                CommentWithoutPostDto commentWithoutPostDto = toCommentWithoutPostDto(comment);

                commentWithoutPostDtos.add(commentWithoutPostDto);
            }
        }

        if (!tags.isEmpty()) {
            for (Tag tag : tags) {
                TagWithoutPostsDto tagWithoutPostsDto = toTagWithoutPostsDto(tag);

                tagWithoutPostsDtos.add(tagWithoutPostsDto);
            }
        }

        postWithComments = toPostWithCommentsDto(post, commentWithoutPostDtos, tagWithoutPostsDtos);

        return postWithComments;
    }

    private CommentWithoutPostDto toCommentWithoutPostDto(Comment comment) {
        CommentWithoutPostDto commentWithoutPostDto = new CommentWithoutPostDto();
        commentWithoutPostDto.setCommentId(comment.getCommentId());
        commentWithoutPostDto.setContent(comment.getContent());
        commentWithoutPostDto.setCreationDate(comment.getCreationDate());
        return commentWithoutPostDto;
    }

    private TagWithoutPostsDto toTagWithoutPostsDto(Tag tag) {
        TagWithoutPostsDto tagWithoutPostsDto = new TagWithoutPostsDto();
        tagWithoutPostsDto.setTagId(tag.getTagId());
        tagWithoutPostsDto.setTagName(tag.getTagName());
        return tagWithoutPostsDto;
    }

    private FullPostDto toPostWithCommentsDto(Post post, List<CommentWithoutPostDto> commentWithoutPostDtos,
                                              Set<TagWithoutPostsDto> tagWithoutPostsDtos) {
        FullPostDto fullPostDto = new FullPostDto();
        fullPostDto.setId(post.getPostId());
        fullPostDto.setTitle(post.getPostTitle());
        fullPostDto.setContent(post.getPostContent());
        fullPostDto.setStar(post.isStar());
        fullPostDto.setComments(commentWithoutPostDtos);
        fullPostDto.setTags(tagWithoutPostsDtos);
        return fullPostDto;
    }
}
