package com.wonders;

import java.io.Serializable;
import java.util.Date;

public class UserBean implements Serializable{

	/**
	 * 
	 */

	private String organ;
	
	private String name;
	
	private String idcard;
	
	private Date birthday;
	
	private String sex;
	
	public UserBean() {
	}

	public UserBean(String organ, String name, String idcard, Date birthday, String sex) {
		super();
		this.organ = organ;
		this.name = name;
		this.idcard = idcard;
		this.birthday = birthday;
		this.sex = sex;
	}

	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
}
