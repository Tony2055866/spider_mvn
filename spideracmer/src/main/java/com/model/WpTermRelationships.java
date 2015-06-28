package com.model;

/**
 * WpTermRelationships entity. @author MyEclipse Persistence Tools
 */

public class WpTermRelationships implements java.io.Serializable {

	// Fields

	private WpTermRelationshipsId id;
	private Integer termOrder;

	// Constructors

	/** default constructor */
	public WpTermRelationships() {
	}

	/** full constructor */
	public WpTermRelationships(WpTermRelationshipsId id, Integer termOrder) {
		this.id = id;
		this.termOrder = termOrder;
	}

	// Property accessors

	public WpTermRelationshipsId getId() {
		return this.id;
	}

	public void setId(WpTermRelationshipsId id) {
		this.id = id;
	}

	public Integer getTermOrder() {
		return this.termOrder;
	}

	public void setTermOrder(Integer termOrder) {
		this.termOrder = termOrder;
	}

}