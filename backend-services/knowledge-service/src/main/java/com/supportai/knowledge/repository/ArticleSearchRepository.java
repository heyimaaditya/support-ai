package com.supportai.knowledge.repository;

import com.supportai.knowledge.domain.ArticleDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface ArticleSearchRepository extends ElasticsearchRepository<ArticleDocument, String> {

    List<ArticleDocument> findByTenantIdAndStatus(String tenantId, String status);
}