package com.model;

/**
 * WpPostmeta entity. @author MyEclipse Persistence Tools
 */

public class WpPostmeta implements java.io.Serializable {

	// Fields

	private Long metaId;
	private Long postId;
	private String metaKey;
	private String metaValue;

	// Constructors

	/** default constructor */
	public WpPostmeta() {
	}

	/** minimal constructor */
	public WpPostmeta(Long postId) {
		this.postId = postId;
	}

	/** full constructor */
	public WpPostmeta(Long postId, String metaKey, String metaValue) {
		this.postId = postId;
		this.metaKey = metaKey;
		this.metaValue = metaValue;
	}

	// Property accessors

	public Long getMetaId() {
		return this.metaId;
	}

	public void setMetaId(Long metaId) {
		this.metaId = metaId;
	}

	public Long getPostId() {
		return this.postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
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