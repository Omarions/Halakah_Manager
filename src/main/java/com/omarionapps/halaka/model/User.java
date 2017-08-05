package com.omarionapps.halaka.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.context.annotation.SessionScope;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Omar on 09-Apr-17.
 */
@Entity
@Table(name = "user")
@SessionScope
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name="email", unique = true, nullable=false)
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an Email")
    private String email;
    @Column(name = "password")
    @Length(min=5, message="*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;
    @Column(name = "name", nullable = false)
    @NotEmpty(message = "*Please provide your name")
    private String name;
    @Column(name = "photo", nullable = false)
    @NotEmpty(message = "*Please provide your name")
    private String photo;
    @Column(name = "title")
    @NotEmpty(message = "*Please provide your title")
    private String title;
    @Column(name = "status", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean status;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return getId() == user.getId();

    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", photo='" + photo + '\'' +
                ", roles=" + roles +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
