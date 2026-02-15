// src/main/java/com/supportai/knowledge/repository/ArticleRepository.java
package com.supportai.knowledge.repository;

import com.supportai.knowledge.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    @Modifying
    @Transactional
    @Query("UPDATE Article a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    void incrementViews(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Article a SET a.upvotes = a.upvotes + 1 WHERE a.id = :id")
    void incrementUpvotes(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Article a SET a.downvotes = a.downvotes + 1 WHERE a.id = :id")
    void incrementDownvotes(Long id);

    List<Article> findTop5ByTenantIdOrderByViewCountDesc(String tenantId);
}