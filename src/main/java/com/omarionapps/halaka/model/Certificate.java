package com.omarionapps.halaka.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Omar on 10-Jun-17.
 */
@Entity
public class Certificate {
    @Id
    @GeneratedValue
    private int id;
    @NotNull
    @NotEmpty
    private String name;
    @OneToOne
    @JoinColumn(name = "student_track_id")
    private StudentTrack studentTrack;
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
    @NotNull
    private String image;
    private String comments;

    public Certificate() {
    }

    public Certificate(Student student, String name, Activity activity, Teacher teacher, Date releaseDate,
                       String image, String comments) {

        this.name = name;
        this.releaseDate = releaseDate;
        this.image = image;
        this.comments = comments;
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

    public StudentTrack getStudentTrack() {
        return studentTrack;
    }

    public void setStudentTrack(StudentTrack studentTrack) {
        this.studentTrack = studentTrack;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id=" + id +
                ", name='" + name + '\'' +

                ", releaseDate=" + releaseDate +
                ", image='" + image + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Certificate)) return false;

        Certificate that = (Certificate) o;

        return getId() == that.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
