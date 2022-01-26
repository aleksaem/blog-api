package com.luxoft.blog.repository;

import com.luxoft.blog.entity.Comment;
import com.luxoft.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Comment findByCommentIdAndPost(Long commentId, Post post);

    public List<Comment> findAllByPost(Post post);
}
