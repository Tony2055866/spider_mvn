package com.model;

import java.sql.Timestamp;

/**
 * Log entity. @author MyEclipse Persistence Tools
 */

public class Log implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String url;
	private Integer flag;
	private Timestamp time;
	private String other;

	// Constructors

	/** default constructor */
	public Log() {
	}

	/** full constructor */
	public Log(String name, String url, Integer flag, Timestamp time,
			String other) {
		this.name = name;
		this.url = url;
		this.flag = flag;
		this.time = time;
		this.other = other;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getOther() {
		return this.other;
	}

	public void setOther(String other) {
		this.other = other;
	}

}