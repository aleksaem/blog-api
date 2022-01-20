package com.luxoft.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long postId;

    @NotBlank(message = "Please Add Post Title")
    private String postTitle;

    @Length(max = 3000)
    @NotBlank(message = "Please Add Content")
    private String postContent;

    @Column(columnDefinition = "boolean default false")
    private boolean star;
}
