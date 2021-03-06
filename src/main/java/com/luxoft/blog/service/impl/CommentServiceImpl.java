package com.luxoft.blog.service.impl;

import com.luxoft.blog.entity.Comment;
import com.luxoft.blog.entity.Post;
import com.luxoft.blog.repository.CommentRepository;
import com.luxoft.blog.repository.PostRepository;
import com.luxoft.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;

    PostRepository postRepository;

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Autowired
    public void setPostRepository(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Comment saveComment(Long postId, Comment comment) {
        Post postFromDB = findPostById(postId);
        comment.setPost(postFromDB);

        return commentRepository.save(comment);
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
    public List<Comment> fetchAllComments(Long postId) {
        Post postfromDB = findPostById(postId);
        return commentRepository.findAllByPost(postfromDB);
    }

    @Override
    public Comment fetchComment(Long postId, Long commentId) {
        Post postFromDB = findPostById(postId);

        return commentRepository.findByCommentIdAndPost(commentId, postFromDB);
    }
}
