package com.example.student;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class People extends BaseBean{
	int id;
	String name;
	String sex;
	int grade;
	int age;
	String password;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getname() {
		return name;
	}
	public void setUsername(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override 
	public String toString() {
		return "User[id = " + id + ", name = " + name +", sex =  "+
				sex + ", grade = " + grade+ ", age = " + age+  ", password = " + password+ "]";
	}
 
}
