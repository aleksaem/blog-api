package com.luxoft.blog.service;

import com.luxoft.blog.dto.CommentWithPostDto;
import com.luxoft.blog.dto.PostWithoutCommentDto;
import com.luxoft.blog.entity.Comment;
import com.luxoft.blog.entity.Post;
import com.luxoft.blog.repository.CommentRepository;
import com.luxoft.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Override
    public CommentWithPostDto saveComment(Long postId, Comment comment) {
        Post postFromDB = findPostById(postId);
        comment.setPost(postFromDB);

        Comment savedComment = commentRepository.save(comment);
        return toCommentWithPostDto(savedComment);
    }

    private Post findPostById(Long postId) {
        Optional<Post> postFromDB = postRepository.findById(postId);
        if (postFromDB.isEmpty()) {
            throw new IllegalArgumentException("Post Cannot Be Found");
        } else {
            return postFromDB.get();
        }
    }

    @Override
    public List<CommentWithPostDto> fetchAllComments(Long postId) {
        Post postfromDB = findPostById(postId);
        List<Comment> comments = commentRepository.findAllByPost(postfromDB);

        List<CommentWithPostDto> commentsDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentsDto.add(toCommentWithPostDto(comment));
        }
        return commentsDto;
    }

    @Override
    public CommentWithPostDto fetchComment(Long postId, Long commentId) {
        Post postFromDB = findPostById(postId);
        Comment comment = commentRepository.findByCommentIdAndPost(commentId, postFromDB);

        return toCommentWithPostDto(comment);
    }

    private CommentWithPostDto toCommentWithPostDto(Comment comment) {
        CommentWithPostDto commentWithPostDto = new CommentWithPostDto();
        commentWithPostDto.setCommentId(comment.getCommentId());
        commentWithPostDto.setContent(comment.getContent());
        commentWithPostDto.setCreationDate(comment.getCreationDate());

        Post post = comment.getPost();
        PostWithoutCommentDto postWithoutCommentDto = new PostWithoutCommentDto();
        postWithoutCommentDto.setPostId(post.getPostId());
        postWithoutCommentDto.setPostTitle(post.getPostTitle());
        postWithoutCommentDto.setPostContent(post.getPostContent());
        postWithoutCommentDto.setStar(post.isStar());

        commentWithPostDto.setPostWithoutCommentDto(postWithoutCommentDto);

        return commentWithPostDto;
    }
}
