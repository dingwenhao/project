package com.util.test.io;

import java.io.Serializable;


public class Person implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -812872967840124841L;
	String name;
	String age;
	transient String sex;//不被序列化
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

}
