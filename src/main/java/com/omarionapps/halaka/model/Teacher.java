package com.omarionapps.halaka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Omar on 30-Apr-17.
 */
@Entity
public class Teacher implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int     id;
	@NotEmpty(message = "Please, add a name...")
	@Column(name = "name", nullable = false)
	private String  name;
	private String  gender;
	private String  photo;
	@NotEmpty(message = "Please, add an email...")
	@Email(message = "Please, add correct email...")
	private String  email;
	@NotEmpty(message = "Please, add phone number...")
	private String  tel;
	private String  education;
	private String  job;
	private String  address;
	private String  resume;
	private Date    hireDate;
	@Column(name = "certified_student", columnDefinition = "TINYINT")
	private boolean certifiedStudent;
	private Date    archivedDate;
	@Column(name = "archived", columnDefinition = "TINYINT")
	private boolean archived;
	private String  comments;
	@OneToMany(mappedBy = "teacher")
	private Set<Course>       courses       = new HashSet<>();
	@OneToMany(mappedBy = "teacher")
	private Set<TeacherTrack> teacherTracks = new HashSet<>();
	@Transient
	private MultipartFile image;
	@Transient
	private String        photoUrl;
	@Transient
	private Set<Activity> activities = new HashSet<>();

	public Teacher() {
	}

	public Teacher(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getJob() {
		return job;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public boolean isCertifiedStudent() {
		return certifiedStudent;
	}

	public void setCertifiedStudent(boolean certifiedStudent) {
		this.certifiedStudent = certifiedStudent;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	@JsonIgnore
	public Set<Activity> getActivities() {
		return this.getCourses().stream().map(m_course -> m_course.getActivity()).collect(Collectors.toSet());
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	@JsonIgnore
	public Set<TeacherTrack> getTeacherTracks() {
		return teacherTracks;
	}

	@JsonIgnore
	public void setTeacherTracks(Set<TeacherTrack> teacherTracks) {
		this.teacherTracks = teacherTracks;
	}

	@Override
	public int hashCode() {
		return getId();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Teacher)) return false;

		Teacher teacher = (Teacher) o;

		return getId() == teacher.getId();

	}

	@Override
	public String toString() {
		String strCourses = courses.stream().map(m_course -> m_course.getName()).collect(Collectors.joining(","));
		return "Teacher{" +
				       "id=" + id +
				       ", name='" + name + '\'' +
				       ", photo='" + photo + '\'' +
				       ", email='" + email + '\'' +
				       ", tel='" + tel + '\'' +
				       ", address='" + address + '\'' +
				       ", education='" + education + '\'' +
				       ", job='" + job + '\'' +
				       ", hire date='" + hireDate + '\'' +
				       ", resume='" + resume + '\'' +
				       ", comments='" + comments + '\'' +
				       ", Courses=[" + strCourses + "]}";
	}


}
