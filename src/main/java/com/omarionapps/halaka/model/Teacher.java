package com.omarionapps.halaka.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Omar on 30-Apr-17.
 */
@Entity
public class Teacher implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinTable(name = "activity_teacher")
    @ManyToMany
    private Set<Activity> activity = new HashSet<>();
    @OneToMany(mappedBy = "teacher")
    private Set<Course> course = new HashSet<>();
    @OneToMany(mappedBy = "teacher")
    private Set<TeacherTrack> teacherTracks = new HashSet<>();
    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;
    private String photo;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String tel;
    private String education;
    private String job;
    private Date hireDate;
    private Date archivedDate;
    @Column(name = "archived", columnDefinition = "TINYINT")
    private boolean archived;
    private String comments;

    public Teacher(){}

    public Teacher(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Set<Activity> getActivity() {
        return activity;
    }

    public void setActivity(Set<Activity> activity) {
        this.activity = activity;
    }

    public Set<Course> getCourse() {
        return course;
    }

    public void setCourse(Set<Course> course) {
        this.course = course;
    }

    public Set<TeacherTrack> getTeacherTracks() {
        return teacherTracks;
    }

    public void setTeacherTracks(Set<TeacherTrack> teacherTracks) {
        this.teacherTracks = teacherTracks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setJob(String job) {
        this.job = job;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
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
        StringBuilder activities = new StringBuilder();
        for (Activity act : activity) {
            activities.append(act.getName()).append(',');
        }
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", education='" + education + '\'' +
                ", comments='" + comments + '\'' +
                ", activities=[" + activities.toString() + "]}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
