package com.luxoft.blog.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.luxoft.blog.dto.CommentWithPostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    @SequenceGenerator(name = "post_sequence",
            sequenceName = "post_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "post_sequence")
    private Long postId;

    @NotBlank(message = "Please Add Post Title")
    private String postTitle;

    @Length(max = 3000)
    @NotBlank(message = "Please Add Content")
    private String postContent;

    @Column(columnDefinition = "boolean default false")
    private boolean star;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<Comment> comments;

}
