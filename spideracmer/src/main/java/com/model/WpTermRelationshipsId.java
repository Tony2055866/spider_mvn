package com.model;

/**
 * WpTermRelationshipsId entity. @author MyEclipse Persistence Tools
 */

public class WpTermRelationshipsId implements java.io.Serializable {

	// Fields

	private Long objectId;
	private Long termTaxonomyId;

	// Constructors

	/** default constructor */
	public WpTermRelationshipsId() {
	}

	/** full constructor */
	public WpTermRelationshipsId(Long objectId, Long termTaxonomyId) {
		this.objectId = objectId;
		this.termTaxonomyId = termTaxonomyId;
	}

	// Property accessors

	public Long getObjectId() {
		return this.objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getTermTaxonomyId() {
		return this.termTaxonomyId;
	}

	public void setTermTaxonomyId(Long termTaxonomyId) {
		this.termTaxonomyId = termTaxonomyId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof WpTermRelationshipsId))
			return false;
		WpTermRelationshipsId castOther = (WpTermRelationshipsId) other;

		return ((this.getObjectId() == castOther.getObjectId()) || (this
				.getObjectId() != null
				&& castOther.getObjectId() != null && this.getObjectId()
				.equals(castOther.getObjectId())))
				&& ((this.getTermTaxonomyId() == castOther.getTermTaxonomyId()) || (this
						.getTermTaxonomyId() != null
						&& castOther.getTermTaxonomyId() != null && this
						.getTermTaxonomyId().equals(
								castOther.getTermTaxonomyId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getObjectId() == null ? 0 : this.getObjectId().hashCode());
		result = 37
				* result
				+ (getTermTaxonomyId() == null ? 0 : this.getTermTaxonomyId()
						.hashCode());
		return result;
	}

}