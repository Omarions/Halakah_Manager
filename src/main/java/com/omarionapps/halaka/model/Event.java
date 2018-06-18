package com.omarionapps.halaka.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Omar on 11-Jul-17.
 */
@Entity
public class Event implements Serializable {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int    id;
	private String name;
	private String introducer;
	private String description;
	private String guests;
	private Date   eventDate;
	private String status;
	private String comments;
    @OneToMany(mappedBy = "event")
    private Set<Certificate> certificates = new HashSet<>();

    public Event() {
    }

	public Event(String name, String introducer, String description, String guests, Date eventDate, String status, String comments, Set<Certificate> certificates) {
		this.name = name;
		this.introducer = introducer;
		this.description = description;
		this.guests = guests;
		this.eventDate = eventDate;
		this.status = status;
		this.comments = comments;
		this.certificates = certificates;
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

	public String getIntroducer() {
		return introducer;
	}

	public void setIntroducer(String introducer) {
		this.introducer = introducer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGuests() {
		return guests;
	}

	public void setGuests(String guests) {
		this.guests = guests;
	}

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(Set<Certificate> certificates) {
        this.certificates = certificates;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        return getId() == event.getId();

    }
}
