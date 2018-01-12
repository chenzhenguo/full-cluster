package com.hhcf.learn.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @Title: Article.java
 * @Description: TODO
 * @author zhaotf
 * @date 2017年12月31日 下午2:50:19
 */
//@Document(indexName = "jdbc-hm-article-mg-", type = "article")
public class Article implements Serializable {
	private static final long serialVersionUID = -7343979092937219653L;
//	@Id
	private Long id;
	/** 标题 */
	private String title;
	/** 摘要 */
	private String abstracts;
	/** 内容 */
	private String content;
	/** 发表时间 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSS'Z'")
//	@Field(type = FieldType.Date, format = DateFormat.basic_date_time, index = true)
//	@CreatedDate
	private Date postTime;
	/** 点击率 */
	private Long clickCount;
	// /** 作者 */
	// private Author author;
	// /** 所属教程 */
	// private Tutorial tutorial;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Long getClickCount() {
		return clickCount;
	}

	public void setClickCount(Long clickCount) {
		this.clickCount = clickCount;
	}

	// public Author getAuthor() {
	// return author;
	// }
	//
	// public void setAuthor(Author author) {
	// this.author = author;
	// }
	//
	// public Tutorial getTutorial() {
	// return tutorial;
	// }
	//
	// public void setTutorial(Tutorial tutorial) {
	// this.tutorial = tutorial;
	// }

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	// setters and getters
	// toString
}
