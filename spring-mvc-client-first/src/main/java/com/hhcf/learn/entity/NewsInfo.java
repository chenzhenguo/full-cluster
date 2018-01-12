package com.hhcf.learn.entity;

import java.io.Serializable;

/**
 * 
 * @Title: NewsInfo.java
 * @Description: TODO
 * @author zhaotf
 * @date 2017年12月31日 下午2:12:36
 */
public class NewsInfo implements Serializable {
	private static final long serialVersionUID = 4770387195202281318L;
	private Long id;
	private String NewsId;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNewsId() {
		return NewsId;
	}

	public void setNewsId(String newsId) {
		NewsId = newsId;
	}

}
