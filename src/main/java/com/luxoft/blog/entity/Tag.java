package com.luxoft.blog.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "tag_sequence",
            sequenceName = "tag_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "tag_sequence")
    private Long tagId;

    @NotBlank(message = "Please add tag name")
    private String tagName;

    @ManyToMany(mappedBy = "tags", cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Set<Post> posts = new HashSet<>();
}
