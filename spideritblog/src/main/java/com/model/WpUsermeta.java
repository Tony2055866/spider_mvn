package com.model;

/**
 * WpUsermeta entity. @author MyEclipse Persistence Tools
 */

public class WpUsermeta implements java.io.Serializable {

	// Fields

	private Long umetaId;
	private Long userId;
	private String metaKey;
	private String metaValue;

	// Constructors

	/** default constructor */
	public WpUsermeta() {
	}

	/** minimal constructor */
	public WpUsermeta(Long userId) {
		this.userId = userId;
	}

	/** full constructor */
	public WpUsermeta(Long userId, String metaKey, String metaValue) {
		this.userId = userId;
		this.metaKey = metaKey;
		this.metaValue = metaValue;
	}

	// Property accessors

	public Long getUmetaId() {
		return this.umetaId;
	}

	public void setUmetaId(Long umetaId) {
		this.umetaId = umetaId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMetaKey() {
		return this.metaKey;
	}

	public void setMetaKey(String metaKey) {
		this.metaKey = metaKey;
	}

	public String getMetaValue() {
		return this.metaValue;
	}

	public void setMetaValue(String metaValue) {
		this.metaValue = metaValue;
	}

}