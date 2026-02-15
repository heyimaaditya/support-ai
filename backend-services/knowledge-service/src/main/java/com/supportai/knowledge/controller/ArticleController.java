package com.supportai.knowledge.controller;

import com.supportai.common.dto.ApiResponse;
import com.supportai.knowledge.domain.Article;
import com.supportai.knowledge.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.supportai.knowledge.domain.ArticleDocument;
import com.supportai.knowledge.repository.ArticleSearchRepository;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ApiResponse<Article>> createArticle(
            @RequestBody Article article,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        article.setTenantId(tenantId);
        Article created = articleService.createArticle(article);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Article created successfully"));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<Article>> getArticle(
            @PathVariable String slug,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        Article article = articleService.getArticleBySlug(slug, tenantId);
        return ResponseEntity.ok(ApiResponse.success(article, "Article retrieved"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Article>> updateArticle(
            @PathVariable Long id,
            @RequestBody Article article) {
        Article updated = articleService.updateArticle(id, article);
        return ResponseEntity.ok(ApiResponse.success(updated, "Article updated"));
    }

@PostMapping("/{id}/view")
public ResponseEntity<Void> trackView(@PathVariable Long id) {
    articleService.trackView(id);
    return ResponseEntity.ok().build();
}

@PostMapping("/{id}/vote")
public ResponseEntity<Void> vote(
        @PathVariable Long id, 
        @RequestParam boolean upvote) {
    articleService.vote(id, upvote);
    return ResponseEntity.ok().build();
}

 @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ArticleDocument>>> search(
            @RequestParam String q,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        List<ArticleDocument> results = articleService.search(q, tenantId);
        return ResponseEntity.ok(ApiResponse.success(results, "Search completed"));
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<Article>>> getPopular(
            @RequestHeader("X-Tenant-Id") String tenantId) {
        List<Article> popular = articleService.getPopularArticles(tenantId);
        return ResponseEntity.ok(ApiResponse.success(popular, "Popular articles retrieved"));
    }
}