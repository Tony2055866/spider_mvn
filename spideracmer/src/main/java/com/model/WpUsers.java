package com.model;

import java.sql.Timestamp;

/**
 * WpUsers entity. @author MyEclipse Persistence Tools
 */

public class WpUsers implements java.io.Serializable {

	// Fields

	private Long id;
	private String userLogin;
	private String userPass;
	private String userNicename;
	private String userEmail;
	private String userUrl;
	private Timestamp userRegistered;
	private String userActivationKey;
	private Integer userStatus;
	private String displayName;

	// Constructors

	/** default constructor */
	public WpUsers() {
	}

	/** full constructor */
	public WpUsers(String userLogin, String userPass, String userNicename,
			String userEmail, String userUrl, Timestamp userRegistered,
			String userActivationKey, Integer userStatus, String displayName) {
		this.userLogin = userLogin;
		this.userPass = userPass;
		this.userNicename = userNicename;
		this.userEmail = userEmail;
		this.userUrl = userUrl;
		this.userRegistered = userRegistered;
		this.userActivationKey = userActivationKey;
		this.userStatus = userStatus;
		this.displayName = displayName;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserLogin() {
		return this.userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserPass() {
		return this.userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getUserNicename() {
		return this.userNicename;
	}

	public void setUserNicename(String userNicename) {
		this.userNicename = userNicename;
	}

	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserUrl() {
		return this.userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

	public Timestamp getUserRegistered() {
		return this.userRegistered;
	}

	public void setUserRegistered(Timestamp userRegistered) {
		this.userRegistered = userRegistered;
	}

	public String getUserActivationKey() {
		return this.userActivationKey;
	}

	public void setUserActivationKey(String userActivationKey) {
		this.userActivationKey = userActivationKey;
	}

	public Integer getUserStatus() {
		return this.userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}