package com.model;

/**
 * WpCommentmeta entity. @author MyEclipse Persistence Tools
 */

public class WpCommentmeta implements java.io.Serializable {

	// Fields

	private Long metaId;
	private Long commentId;
	private String metaKey;
	private String metaValue;

	// Constructors

	/** default constructor */
	public WpCommentmeta() {
	}

	/** minimal constructor */
	public WpCommentmeta(Long commentId) {
		this.commentId = commentId;
	}

	/** full constructor */
	public WpCommentmeta(Long commentId, String metaKey, String metaValue) {
		this.commentId = commentId;
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

	public Long getCommentId() {
		return this.commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
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