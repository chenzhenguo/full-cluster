package com.hhcf.learn.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.hhcf.learn.entity.Article;

/**
 * 
 * @Title: ArticleSearchRepository.java
 * @Description: TODO
 * @author zhaotf
 * @date 2017年12月31日 下午3:55:50
 * @see {@linkplain http://www.tianshouzhi.com/api/tutorials/springboot/101}
 */
public interface ArticleSearchRepository extends ElasticsearchRepository<Article, Long> {

}
