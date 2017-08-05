package com.omarionapps.halaka.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Date;

/**
 * Created by Omar on 01-May-17.
 */
@Entity
public class Task {
    @Id
    @GeneratedValue
    private int id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "DATETIME")
    private Date createDate;
    @Column(columnDefinition = "DATETIME")
    private Date dueDate;
    @Size(min = 0, max = 10)
    private int progress;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    public Task(){}
}
