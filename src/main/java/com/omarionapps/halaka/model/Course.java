package com.omarionapps.halaka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Omar on 30-Apr-17.
 */
@Entity
public class Course implements Serializable {
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
    @NotEmpty(message = "It could not be empty!")
    private String     name;
    @NotEmpty(message = "It could not be empty!")
    private String     days;
    @Column(name = "start_time", nullable = false)
    private Time       startTime;
    @Column(name = "end_time", nullable = false)
    private Time       endTime;
    @Column(name = "start_date", nullable = true)
	private Date       startDate;
    @Column(name = "end_date", nullable = true)
	private Date       endDate;
    @Column(name = "capacity")
    @Min(value = 0)
	private int        capacity;
    @Column(name = "comments", nullable = true)
    private String     comments;
    @Column(name = "archived", columnDefinition = "TINYINT")
    private boolean    archived;
    @Column(name = "archived_date", nullable = true)
    private Date       archivedDate;
    @Transient
    private boolean    full;
    @Transient
    private int        freePlaces;
    @Transient
    private WeekDays[] courseDays;
	

    public Course(){}

    public Course(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WeekDays[] getCourseDays() {
        return courseDays;
    }

    public void setCourseDays(WeekDays[] courseDays) {
        this.courseDays = courseDays;
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

    @JsonIgnore
    public Teacher getTeacher() {
        return teacher;
    }

    @JsonIgnore
    public void setTeacher(Teacher teachers) {
        this.teacher = teachers;
    }

    @JsonIgnore
    public Activity getActivity() {
        return activity;
    }

    @JsonIgnore
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @JsonIgnore
    public Set<CourseTrack> getCourseTracks() {
        return courseTracks;
    }

    @JsonIgnore
    public void setCourseTracks(Set<CourseTrack> courseTracks) {
        this.courseTracks = courseTracks;
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

    public boolean isFull() {
        long occupied = getStudentTracks()
                .stream()
                .filter(st -> st.getStatus().equals(StudentStatus.STUDYING.toString()) ||
                        st.getStatus().equals(StudentStatus.TEMP_STOP.toString()))
                .count();
        System.out.println("Course (" + this.id + ") has occupied seats = " + occupied);
        return (occupied == capacity);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

	public long getFreePlaces() {
        long occupied = getStudentTracks()
                .stream()
                .filter(st -> st.getStatus().equals(StudentStatus.STUDYING.toString()) ||
                        st.getStatus().equals(StudentStatus.TEMP_STOP.toString()))
                .count();

		return (getCapacity() - occupied);
	}

    @JsonIgnore
    public void setStudentTracks(Set<StudentTrack> studentTracks) {
        this.studentTracks = studentTracks;
    }

	@JsonIgnore
	public Set<StudentTrack> getStudentTracks() {
		return studentTracks;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;

        Course course = (Course) o;

        return getId() == course.getId();

    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                       ", activity:(" + activity.getId() + "|" + activity.getName() + ")" +
                ", teacherName:'" + teacher.getName() + "'\'" +
                ", name:'" + name + "\'" +
                ", days:'" + days + "\'" +
                ", capacity:'" + capacity + "\'" +
                ", startDate:'" + startDate + "\'" +
                ", endDate:'" + endDate + "\'" +
                ", startTime:'" + startTime + "\'" +
                ", endTime:'" + endTime + "\'" +
                ", comments:'" + comments + "\'}";

    }
}
