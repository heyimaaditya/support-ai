// src/main/java/com/supportai/knowledge/service/ArticleService.java
package com.supportai.knowledge.service;

import com.supportai.common.exception.ResourceNotFoundException;
import com.supportai.knowledge.domain.Article;
import com.supportai.knowledge.domain.ArticleDocument;
import com.supportai.knowledge.repository.ArticleRepository;
import com.supportai.knowledge.repository.ArticleSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleSearchRepository articleSearchRepository;

    @Transactional
    public Article createArticle(Article article) {
        article.setSlug(generateSlug(article.getTitle()));
        Article saved = articleRepository.save(article);
        syncToElasticsearch(saved);
        return saved;
    }

    private void syncToElasticsearch(Article article) {
        ArticleDocument doc = ArticleDocument.builder()
                .id(article.getId().toString())
                .title(article.getTitle())
                .content(article.getContent())
                .tenantId(article.getTenantId())
                .status(article.getStatus())
                .build();
        articleSearchRepository.save(doc);
    }

    public void trackView(Long id) {
        articleRepository.incrementViews(id);
    }

    public void vote(Long id, boolean upvote) {
        if (upvote) articleRepository.incrementUpvotes(id);
        else articleRepository.incrementDownvotes(id);
    }

    @Cacheable(value = "popular_articles", key = "#tenantId")
    public List<Article> getPopularArticles(String tenantId) {
        return articleRepository.findTop5ByTenantIdOrderByViewCountDesc(tenantId);
    }

    public List<ArticleDocument> search(String query, String tenantId) {
  
        return (List<ArticleDocument>) articleSearchRepository.findAll(); 
    }

    private String generateSlug(String input) {
        if (input == null) return "";
        String nowhitespace = input.replaceAll("\\s+", "-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        return normalized.replaceAll("[^\\w-]", "").toLowerCase(Locale.ENGLISH);
    }

    public Article updateArticle(Long id, Article updated) {
        Article existing = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with id: " + id));
        existing.setTitle(updated.getTitle());
        existing.setContent(updated.getContent());
        existing.setStatus(updated.getStatus());
        existing.setCategory(updated.getCategory());
        existing.setTags(updated.getTags());
        Article saved = articleRepository.save(existing);
        syncToElasticsearch(saved);
        return saved;
    }

    public Article getArticleBySlug(String slug, String tenantId) {
        return articleRepository.findAll().stream()
                .filter(a -> a.getSlug().equals(slug) && a.getTenantId().equals(tenantId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with slug: " + slug));
    }
}