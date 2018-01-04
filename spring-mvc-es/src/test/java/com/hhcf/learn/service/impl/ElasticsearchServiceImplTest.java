package com.hhcf.learn.service.impl;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.BaseJunitTest;
import com.hhcf.learn.entity.Article;
import com.hhcf.learn.service.ElasticsearchService;

/**
 * 
 * @Title: ElasticsearchServiceImplTest.java
 * @Description: TODO
 * @author zhaotf
 * @date 2017年12月31日 下午4:11:55
 */
public class ElasticsearchServiceImplTest extends BaseJunitTest {
	@Autowired
	private ElasticsearchService elasticsearchService;

	@Test
	public void findIndex() {
		try {
			elasticsearchService.findIndex("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("eeeeeeeeeeeeee");
	}

//	@Autowired
//	private ArticleSearchRepository articleSearchRepository;

	@Test
	public void testSaveArticleIndex() {
		// Author author = new Author();
		// author.setId(1L);
		// author.setName("tianshouzhi");
		// author.setRemark("java developer");
		//
		// Tutorial tutorial = new Tutorial();
		// tutorial.setId(1L);
		// tutorial.setName("elastic search");

		Article article = new Article();
		article.setId(3L);
		article.setTitle("springboot integreate elasticsearch");
		article.setAbstracts("springboot integreate elasticsearch is very easy");
		// article.setTutorial(tutorial);
		// article.setAuthor(author);
		article.setContent("elasticsearch based on lucene," + "spring-data-elastichsearch based on elaticsearch"
				+ ",this tutorial tell you how to integrete springboot with spring-data-elasticsearch");
		article.setPostTime(new Date());
		article.setClickCount(1L);

//		articleSearchRepository.save(article);
	}
}
