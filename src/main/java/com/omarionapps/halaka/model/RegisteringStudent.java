package com.omarionapps.halaka.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omar on 23-Sep-17.
 */

public class RegisteringStudent extends Student {
	private MultipartFile image;
	private List<Wish> wishes = new ArrayList<>();

	public RegisteringStudent() {
	}

	public RegisteringStudent(List<Wish> wishes, MultipartFile image) {
		this.wishes = wishes;
		this.setImage(image);
	}

	public List<Wish> getWishes() {
		return wishes;
	}

	public void setWishes(List<Wish> wishes) {
		this.wishes = wishes;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}
}
