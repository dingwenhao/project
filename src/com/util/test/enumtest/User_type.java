package com.util.test.enumtest;

public enum User_type {
	
		EXPERT("刘建标",23),
		DOCTOR("刘建标",23),
		STUDENT("刘建标",23),
		WORKER("刘建标",23),
		COMMON("刘建标",23);
		private String name;
		private int age;
		
	private User_type(String name,int age) {
		this.name=name;
		this.age=age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}
