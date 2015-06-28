package com.model;

import java.util.HashSet;
import java.util.Set;

/**
 * WpTermTaxonomy entity. @author MyEclipse Persistence Tools
 */

public class WpTermTaxonomy implements java.io.Serializable {

	// Fields

	private Long termTaxonomyId;
	private Long termId;
	private String taxonomy;
	private String description;
	private Long parent;
	private Long count;

	private WpTerms term;
	private Set<WpPosts> posts = new HashSet<WpPosts>();
	// Constructors

	/** default constructor */
	public WpTermTaxonomy() {
	}

	/** full constructor */
	public WpTermTaxonomy(Long termId, String taxonomy, String description,
			Long parent, Long count) {
		this.termId = termId;
		this.taxonomy = taxonomy;
		this.description = description;
		this.parent = parent;
		this.count = count;
	}

	// Property accessors

	
	public Long getTermTaxonomyId() {
		return this.termTaxonomyId;
	}

	public Set<WpPosts> getPosts() {
		return posts;
	}

	public void setPosts(Set<WpPosts> posts) {
		this.posts = posts;
	}

	public WpTerms getTerm() {
		return term;
	}

	public void setTerm(WpTerms term) {
		this.term = term;
	}

	public void setTermTaxonomyId(Long termTaxonomyId) {
		this.termTaxonomyId = termTaxonomyId;
	}

	public Long getTermId() {
		return this.termId;
	}

	public void setTermId(Long termId) {
		this.termId = termId;
	}

	public String getTaxonomy() {
		return this.taxonomy;
	}

	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getParent() {
		return this.parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public Long getCount() {
		return this.count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}