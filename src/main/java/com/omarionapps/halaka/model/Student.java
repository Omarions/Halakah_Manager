package com.omarionapps.halaka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Omar on 09-Apr-17.
 */
@Entity
public class Student {
    @Id
    @GeneratedValue
    private int id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String gender;
    private String photo;
    private String identityId;
    private Date birthDate;
    private String birthLocation;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;
    private String egyptAddress;
    private String homeAddress;
    @NotEmpty
    private String tel;
    @NotEmpty
    @Email
    private String email;
    private String facebook;
    private String education;
    private String job;
    @Column(name = "archived", columnDefinition = "TINYINT")
    private boolean archived;
    @Column(name = "archived_date", nullable = true)
    private Date archivedDate;
    @ManyToOne
    @JoinColumn(name = "house_id", referencedColumnName = "id")
    private House house;
    private String comments;
    @OneToMany(mappedBy = "student")
    private List<CourseTrack> courseTracks = new ArrayList<>();
    @OneToMany(mappedBy = "student")
    private List<StudentTrack> studentTracks = new ArrayList<>();
    @Transient
    private List<Certificate> certificates = new ArrayList<>();
    @Transient
    private List<Course> courses = new ArrayList<>();
    @Transient
    private String photoUrl;

    public Student(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
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

	public Country getCountry() {
		return country;
	}

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getEgyptAddress() {
        return egyptAddress;
    }

    public void setEgyptAddress(String egyptAddress) {
        this.egyptAddress = egyptAddress;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
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

    public void setJob(String job) {
        this.job = job;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public Date getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(Date archivedDate) {
        this.archivedDate = archivedDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<CourseTrack> getCourseTracks() {
        return courseTracks;
    }

    public void setCourseTracks(List<CourseTrack> courseTracks) {
        this.courseTracks = courseTracks;
    }

    public List<Certificate> getCertificates() {
	    return this.getStudentTracks().stream()
			    .filter((st) -> st.getStudent().equals(this) && st.getStatus().equalsIgnoreCase(StudentStatus.CERTIFIED.toString()))
			    .filter(studentTracks -> studentTracks.getCertificate() != null)
			    .map(studentTracks -> studentTracks.getCertificate()).collect(Collectors.toList());
        
    }

    public List<StudentTrack> getStudentTracks() {
        return studentTracks;
    }

    public void setStudentTracks(List<StudentTrack> studentTracks) {
        this.studentTracks = studentTracks;
    }

    @JsonIgnore
    public List<Course> getCourses() {
        courses.clear();
        this.getStudentTracks().stream()
            .filter((st) -> st.getStudent() == this)
            .forEach((st) -> courses.add(st.getCourse()));
        return courses;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthLocation() {
        return birthLocation;
    }

    public void setBirthLocation(String birthLocation) {
        this.birthLocation = birthLocation;
    }

    @JsonIgnore
    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        return id == student.id;

    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Student{")
                .append("id=").append(id)
		        .append(", name='" + name + "\'")
		        .append(", photo='" + photo + "\'")
                .append(", identityID='" + identityId + '\'')
                .append(", egyptAddress='" + egyptAddress + "\'")
                .append(", homeAddress='" + homeAddress + '\'')
                .append(", tel='" + tel + '\'')
                .append(", education='" + education + '\'')
                .append(", job='" + job + '\'')
                .append(", country=[" + country.getEnglishName() + "]")
                .append(", courseTracks[ " + courseTracks + "]")
                .append(", StudentTracks[");

        for (StudentTrack track : studentTracks) {
	        result.append("Track[id=" + track.getId() + "], ]}");
            /*
                    .append(", course=[" + track.getCourses().getName() + "]")
                    .append(", registerDate=" + track.getRegisterDate())
                    .append(", startDate=" + track.getStartDate())
                    .append(", status='" + track.getStatus())
                    .append(", evaluation=" + track.getEvaluation())
                    .append(", comments='" + comments + "]]}");
            */
        }
        return result.toString();
    }
}
