package com.omarionapps.halaka.model;

import org.hibernate.id.IncrementGenerator;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.sql.Time;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Omar on 30-Apr-17.
 */
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    private Activity activity;
    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;
    @OneToMany(mappedBy = "course")
    private Set<CourseTrack> courseTracks = new HashSet<>();
    @OneToMany(mappedBy = "course")
    private Set<StudentTrack> studentTracks = new HashSet<>();
    @NotEmpty
    private String name;
    @NotEmpty
    private String days;
    @Column(name = "start_date", nullable = true)
    private Date startDate;
    @Column(name = "end_date", nullable = true)
    private Date endDate;
    @Column(name = "start_time", nullable = false)
    private Time startTime;
    @Column(name = "end_time", nullable = false)
    private Time endTime;
    private int capacity;
    @Column(name = "comments", nullable = true)
    private String comments;
    @Transient
    private boolean full;
    @Transient
    private int freePlaces;

    public Course(){}

    public Course(String name){
        this.name = name;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teachers) {
        this.teacher = teachers;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<StudentTrack> getStudentTracks() {
        return studentTracks;
    }

    public void setStudentTracks(Set<StudentTrack> studentTracks) {
        this.studentTracks = studentTracks;
    }

    public Set<CourseTrack> getCourseTracks() {
        return courseTracks;
    }

    public void setCourseTracks(Set<CourseTrack> courseTracks) {
        this.courseTracks = courseTracks;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isFull() {
        long currentStudy = getStudentTracks()
                .stream()
                .filter((st) -> st.getStatus().equalsIgnoreCase(StudentStatus.STUDYING.toString())
                                && st.getCourse().getId() == this.getId())
                .count();
        return (currentStudy == getCapacity());
    }

    public long getFreePlaces(){
        long currentStudy = getStudentTracks().stream().filter((st) -> st.getStatus().equalsIgnoreCase(StudentStatus.STUDYING.toString())).count();
        return (getCapacity() - currentStudy);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", activityName='" + activity.getName() + "'\'" +
                ", teacheName='" + teacher.getName() + "'\'" +
                ", name='" + name + "\'" +
                ", days='" + days + "\'" +
                ", capacity='" + capacity + "\'" +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", comments='" + comments + "\'}";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;

        Course course = (Course) o;

        return getId() == course.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
