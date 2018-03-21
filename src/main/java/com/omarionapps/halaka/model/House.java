package com.omarionapps.halaka.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Created by Omar on 15-Apr-17.
 */
@Entity
public class House {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private int capacity;
    private int occupied;
    private String comments;
    @OneToMany(mappedBy = "house")
    private Set<Student> students;

	public House() {
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "House{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", occupied=" + occupied +
                ", comments='" + comments + '\'' +
                ", Students Count=" + students.size() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof House)) return false;

        House house = (House) o;

        return getId() == house.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
