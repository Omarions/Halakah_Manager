package com.omarionapps.halaka.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Omar on 16-Apr-17.
 */
@Entity
public class Activity{
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String comments;
    private Date startDate;
    private Date archivedDate;
    @JoinTable(name = "activity_teacher")
    @ManyToMany
    private Set<Teacher> teacher = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activity")
    private Set<Course> courses = new HashSet<>();
    @Column(name = "archived", columnDefinition = "TINYINT")
    private boolean archived;

    public Activity(){}

    public Activity(String name, String comments) {
        this.name = name;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Set<Teacher> getTeacher() {
        return teacher;
    }

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

    @Override
    public String toString() {
        StringBuilder teachers = new StringBuilder();
        StringBuilder activityCourses = new StringBuilder();

        teacher.forEach((t) -> teachers.append(t.getName()).append(','));
        courses.forEach((c) -> activityCourses.append(c.getName()).append(','));

        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", comments='" + comments + '\'' +
                ", teachers=[" + teachers + "]" +
                ", courses=[" + activityCourses + "]}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;

        Activity activity = (Activity) o;

        return getId() == activity.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
