package com.omarionapps.halaka.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Omar on 03-May-17.
 */
@Entity
public class StudentTrack implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;
    @OneToOne(mappedBy = "studentTrack")
    private Certificate certificate;
    @Temporal(TemporalType.DATE)
    private Date registerDate;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    private String status;
    private int evaluation;
    private String comments;

    public StudentTrack(){}

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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "StudentTrack{" +
                "id=" + id +
                ", student=" + student +
                ", course=" + course.getName() +
                ", registerDate=" + registerDate +
                ", startDate=" + startDate +
                ", status='" + status + '\'' +
                ", evaluation=" + evaluation +
                ", comments='" + comments + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentTrack)) return false;

        StudentTrack that = (StudentTrack) o;

        return getId() == that.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
