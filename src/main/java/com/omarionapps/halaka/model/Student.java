package com.omarionapps.halaka.model;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Omar on 09-Apr-17.
 */
@Entity
public class Student {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String identityId;
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    private String birthLocation;
    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;
    private String egyptAddress;
    private String homeAddress;
    private String tel;
    @Email
    private String email;
    private String facebook;
    private String education;
    private String job;
    @ManyToOne
    @JoinColumn(name = "house_id", referencedColumnName = "id")
    private House house;
    private String comments;
    @OneToMany(mappedBy = "student")
    private Set<CourseTrack> courseTracks = new HashSet<>();
    @OneToMany(mappedBy = "student")
    private Set<StudentTrack> studentTracks = new HashSet<>();
    @Transient
    private List<Certificate> certificates = new ArrayList<>();
    @Transient
    private List<Course> courses = new ArrayList<>();

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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<CourseTrack> getCourseTracks() {
        return courseTracks;
    }

    public void setCourseTracks(Set<CourseTrack> courseTracks) {
        this.courseTracks = courseTracks;
    }

    public Set<StudentTrack> getStudentTracks() {
        return studentTracks;
    }

    public void setStudentTracks(Set<StudentTrack> studentTracks) {
        this.studentTracks = studentTracks;
    }

    public List<Certificate> getCertificates() {
        certificates.clear();
        this.getStudentTracks().stream()
                .filter((st) -> st.getStudent() == this)
                .forEach((st) -> certificates.add(st.getCertificate()));
        return certificates;
    }

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

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Student{")
                .append("id=").append(id)
                .append(", name=" + name + "\'")
                .append(", identityID='" + identityId + '\'')
                .append(", egyptAddress='" + egyptAddress + "\'")
                .append(", homeAddress='" + homeAddress + '\'')
                .append(", tel='" + tel + '\'')
                .append(", education='" + education + '\'')
                .append(", job='" + job + '\'')
                .append(", country=[" + country.getEnglishName() + "]" )
                .append(", courseTracks[ " + courseTracks + "]")
                .append(", StudentTracks[");

        for (StudentTrack track : studentTracks){
            result.append("Track[id=" + track.getId())
                    .append(", course=[" + track.getCourse().getName() + "]")
                    .append(", registerDate=" + track.getRegisterDate())
                    .append(", startDate=" + track.getStartDate())
                    .append(", status='" + track.getStatus())
                    .append(", evaluation=" + track.getEvaluation())
                    .append(", comments='" + comments  + "]]}");
        }
                return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        return id == student.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
