package com.supportai.knowledge.domain;

import com.supportai.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "articles")
@Getter 
@Setter 
@NoArgsConstructor
public class Article extends BaseEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String slug;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String status;
    private String tenantId;
    private Integer upvotes = 0;
private Integer downvotes = 0;

    private Integer viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
        name = "article_tags",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
}