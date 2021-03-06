package com.omarionapps.halaka.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omar on 23-Sep-17.
 */
public class RegisteringStudent {
	private int     id;
	private Student student;
	private List<Wish> wishes = new ArrayList<>();

	public RegisteringStudent() {
	}

	public RegisteringStudent(int id, Student student, List<Wish> wishes) {
		this.id = id;
		this.student = student;
		this.wishes = wishes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public List<Wish> getWishes() {
		return wishes;
	}

	public void setWishes(List<Wish> wishes) {
		this.wishes = wishes;
	}
}
