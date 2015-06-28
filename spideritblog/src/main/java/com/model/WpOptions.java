package com.model;

/**
 * WpOptions entity. @author MyEclipse Persistence Tools
 */

public class WpOptions implements java.io.Serializable {

	// Fields

	private Long optionId;
	private String optionName;
	private String optionValue;
	private String autoload;

	// Constructors

	/** default constructor */
	public WpOptions() {
	}

	/** full constructor */
	public WpOptions(String optionName, String optionValue, String autoload) {
		this.optionName = optionName;
		this.optionValue = optionValue;
		this.autoload = autoload;
	}

	// Property accessors

	public Long getOptionId() {
		return this.optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	public String getOptionName() {
		return this.optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getOptionValue() {
		return this.optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public String getAutoload() {
		return this.autoload;
	}

	public void setAutoload(String autoload) {
		this.autoload = autoload;
	}

}