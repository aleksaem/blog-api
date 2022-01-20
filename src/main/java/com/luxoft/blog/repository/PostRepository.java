package com.luxoft.blog.repository;

import com.luxoft.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    public Optional<Post> findByPostTitle(String postTitle);

    public List<Post> findByPostTitleIgnoreCase(String postTitle);

    public List<Post> findByStar(boolean star);
}
