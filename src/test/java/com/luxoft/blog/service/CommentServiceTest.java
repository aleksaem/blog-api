package com.luxoft.blog.service;

import com.luxoft.blog.entity.Comment;
import com.luxoft.blog.entity.Post;
import com.luxoft.blog.repository.CommentRepository;
import com.luxoft.blog.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentServiceTest {

    @MockBean
    CommentRepository commentRepository;

    @MockBean
    PostRepository postRepository;

    @Autowired
    CommentService commentService;

    @Test
    public void saveCommentTest() {
        Post post = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .build();

        Comment comment = Comment.builder()
                .commentId(1L)
                .creationDate(LocalDateTime.now())
                .content("Comment")
                .post(post)
                .build();

        when(commentRepository.save(comment)).thenReturn(comment);
        when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(post));

        Comment savedComment = commentService.saveComment(1L, comment);

        verify(commentRepository, times(1)).save(comment);
        assertEquals("Comment", savedComment.getContent());
        assertEquals(comment.getCreationDate(), savedComment.getCreationDate());
        assertEquals(post, savedComment.getPost());
    }

    @Test
    public void fetchAllCommentsToPostTest() {
        Post post = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .build();

        Comment comment_1 = Comment.builder()
                .commentId(1L)
                .creationDate(LocalDateTime.now())
                .content("Comment 1")
                .post(post)
                .build();

        Comment comment_2 = Comment.builder()
                .commentId(2L)
                .creationDate(LocalDateTime.now())
                .content("Comment 2")
                .post(post)
                .build();

        Comment comment_3 = Comment.builder()
                .commentId(3L)
                .creationDate(LocalDateTime.now())
                .content("Comment 3")
                .post(post)
                .build();

        List<Comment> comments = List.of(comment_1, comment_2, comment_3);
        when(commentRepository.findAllByPost(post)).thenReturn(comments);
        when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(post));

        List<Comment> foundComments = commentService.fetchAllComments(1L);
        verify(commentRepository, times(1)).findAllByPost(post);
        assertEquals(3, foundComments.size());
        assertEquals("Comment 2", foundComments.get(1).getContent());
        assertEquals(3L, foundComments.get(2).getCommentId());
        assertEquals(post, foundComments.get(0).getPost());
    }

    @Test
    public void fetchCommentToPostByCommentId() {
        Post post = Post.builder()
                .postId(1L)
                .postTitle("Post")
                .postContent("Content")
                .star(true)
                .build();

        Comment comment = Comment.builder()
                .commentId(2L)
                .creationDate(LocalDateTime.now())
                .content("Comment 2")
                .post(post)
                .build();

        when(commentRepository.findByCommentIdAndPost(2L, post)).thenReturn(comment);
        when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(post));

        Comment foundComment = commentService.fetchComment(1L, 2L);
        verify(commentRepository, times(1)).findByCommentIdAndPost(2L, post);
        assertEquals("Comment 2", foundComment.getContent());
        assertEquals(2L, foundComment.getCommentId());
        assertEquals(post, foundComment.getPost());
    }
}
