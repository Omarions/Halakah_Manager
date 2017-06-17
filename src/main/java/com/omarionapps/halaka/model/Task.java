package com.omarionapps.halaka.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/**
 * Created by Omar on 01-May-17.
 */
@Entity
public class Task {
    @Id
    @GeneratedValue
    private int id;

    public Task(){}
}
