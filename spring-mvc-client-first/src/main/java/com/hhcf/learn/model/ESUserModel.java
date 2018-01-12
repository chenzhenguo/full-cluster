package com.hhcf.learn.model;

import java.io.Serializable;

/**
 * 
 * @Title: ESUserModel
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月2日 下午4:36:08
 */
public class ESUserModel implements Serializable {
	private static final long serialVersionUID = 5177682516330390930L;
	private Long id;
	private String name;
	private String location;// 位置
	private Double lat;// 纬度latitude
	private Double lon;// 经度longitude

	public ESUserModel() {
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param dlat
	 *            纬度latitude
	 * @param dlon
	 *            经度longitude
	 */
	public ESUserModel(Long id, String name, Double dlat, Double dlon) {
		this.id = Long.valueOf(id);
		this.name = name;
		this.lat = dlat;
		this.lon = dlon;
		// this.location = dlat + "," + dlon;//TODO,顺序：经前纬后
		this.location = dlon + "," + dlat;// TODO,顺序：经前纬后
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
