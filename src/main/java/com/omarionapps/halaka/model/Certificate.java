package com.omarionapps.halaka.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;
    @NotNull
    private String image;
    private String comments;

    public Certificate() {
    }

    public Certificate(String name, Event event,
                       String image, String comments) {

        this.name = name;
        this.event = event;
        this.image = image;
        this.comments = comments;
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Certificate)) return false;

        Certificate that = (Certificate) o;

        return getId() == that.getId();

    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", event='" + event.getName() + '\'' +
                ", image='" + image + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
