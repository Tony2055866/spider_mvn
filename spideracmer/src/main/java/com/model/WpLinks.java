package com.model;

import java.sql.Timestamp;

/**
 * WpLinks entity. @author MyEclipse Persistence Tools
 */

public class WpLinks implements java.io.Serializable {

	// Fields

	private Long linkId;
	private String linkUrl;
	private String linkName;
	private String linkImage;
	private String linkTarget;
	private String linkDescription;
	private String linkVisible;
	private Long linkOwner;
	private Integer linkRating;
	private Timestamp linkUpdated;
	private String linkRel;
	private String linkNotes;
	private String linkRss;

	// Constructors

	/** default constructor */
	public WpLinks() {
	}

	/** full constructor */
	public WpLinks(String linkUrl, String linkName, String linkImage,
			String linkTarget, String linkDescription, String linkVisible,
			Long linkOwner, Integer linkRating, Timestamp linkUpdated,
			String linkRel, String linkNotes, String linkRss) {
		this.linkUrl = linkUrl;
		this.linkName = linkName;
		this.linkImage = linkImage;
		this.linkTarget = linkTarget;
		this.linkDescription = linkDescription;
		this.linkVisible = linkVisible;
		this.linkOwner = linkOwner;
		this.linkRating = linkRating;
		this.linkUpdated = linkUpdated;
		this.linkRel = linkRel;
		this.linkNotes = linkNotes;
		this.linkRss = linkRss;
	}

	// Property accessors

	public Long getLinkId() {
		return this.linkId;
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

	public String getLinkUrl() {
		return this.linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkName() {
		return this.linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkImage() {
		return this.linkImage;
	}

	public void setLinkImage(String linkImage) {
		this.linkImage = linkImage;
	}

	public String getLinkTarget() {
		return this.linkTarget;
	}

	public void setLinkTarget(String linkTarget) {
		this.linkTarget = linkTarget;
	}

	public String getLinkDescription() {
		return this.linkDescription;
	}

	public void setLinkDescription(String linkDescription) {
		this.linkDescription = linkDescription;
	}

	public String getLinkVisible() {
		return this.linkVisible;
	}

	public void setLinkVisible(String linkVisible) {
		this.linkVisible = linkVisible;
	}

	public Long getLinkOwner() {
		return this.linkOwner;
	}

	public void setLinkOwner(Long linkOwner) {
		this.linkOwner = linkOwner;
	}

	public Integer getLinkRating() {
		return this.linkRating;
	}

	public void setLinkRating(Integer linkRating) {
		this.linkRating = linkRating;
	}

	public Timestamp getLinkUpdated() {
		return this.linkUpdated;
	}

	public void setLinkUpdated(Timestamp linkUpdated) {
		this.linkUpdated = linkUpdated;
	}

	public String getLinkRel() {
		return this.linkRel;
	}

	public void setLinkRel(String linkRel) {
		this.linkRel = linkRel;
	}

	public String getLinkNotes() {
		return this.linkNotes;
	}

	public void setLinkNotes(String linkNotes) {
		this.linkNotes = linkNotes;
	}

	public String getLinkRss() {
		return this.linkRss;
	}

	public void setLinkRss(String linkRss) {
		this.linkRss = linkRss;
	}

}