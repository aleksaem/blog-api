package com.luxoft.blog.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    @EqualsAndHashCode.Include
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

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinTable(name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @Override
    public String toString() {
        Set<String> tagNames = new HashSet<>();
        for (Tag tag : tags) {
            tagNames.add(tag.getTagName());
        }
        return "Post{" +
                "postId=" + postId +
                ", postTitle='" + postTitle + '\'' +
                ", postContent='" + postContent + '\'' +
                ", star=" + star +
                ", comments=" + comments +
                ", tags=" + tagNames +
                '}';
    }
}
