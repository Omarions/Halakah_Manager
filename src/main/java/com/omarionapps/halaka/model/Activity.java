package com.omarionapps.halaka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.omarionapps.halaka.controller.PhotoController;
import com.omarionapps.halaka.utils.LocationTag;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Omar on 16-Apr-17.
 */
@Entity
public class Activity implements Serializable {
	@Id
	@GeneratedValue
	private int    id;
	private String name;
	private String comments;
	private String logo;
	private Date   startDate;
	private Date   archivedDate;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "activity")
	private Set<Course>  courses = new HashSet<>();
	@Column(name = "archived", columnDefinition = "TINYINT")
	private boolean       archived;
	@Transient
	private Set<Teacher> teacher = new HashSet<>();
	@Transient
	private MultipartFile logoFile;
	@Transient
	private String        logoUrl;


	public Activity() {
	}

	public Activity(String name) {
		this.name = name;
	}

	public Activity(String name, String comments) {
		this.name = name;
		this.comments = comments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@JsonIgnore
	public Set<Course> getCourses() {
		return courses;
	}

	@JsonIgnore
	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	@JsonIgnore
	public Set<Teacher> getTeacher() {
		return teacher;
	}

	@JsonIgnore
	public void setTeacher(Set<Teacher> teacher) {
		this.teacher = teacher;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getArchivedDate() {
		return archivedDate;
	}

	public void setArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public MultipartFile getLogoFile() {
		return logoFile;
	}

	public void setLogoFile(MultipartFile logoFile) {
		this.logoFile = logoFile;
	}

	public String getLogoUrl() {
		String url = MvcUriComponentsBuilder
				.fromMethodName(PhotoController.class, "getFile", this.getLogo(), LocationTag.ACTIVITY_STORE_LOC).build().toString();
		logoUrl = url;
		return logoUrl;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Override
	public int hashCode() {
		return getId();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Activity)) return false;

		Activity activity = (Activity) o;

		return getId() == activity.getId();

	}

	@Override
	public String toString() {
		String strCourses = courses.stream().map(m_course -> m_course.getName()).collect(Collectors.joining(","));
		return "Activity{" +
				"id=" + id +
				", name='" + name + '\'' +
				", comments='" + comments + '\'' +
				", logo= '" + logo + '\'' +
				", courses=[" + strCourses + "]}";
	}
}
