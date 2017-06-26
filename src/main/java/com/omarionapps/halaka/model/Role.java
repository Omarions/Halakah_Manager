package com.omarionapps.halaka.model;

import org.springframework.web.context.annotation.SessionScope;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Omar on 22-Apr-17.
 */
@Entity
@Table(name = "role")
@SessionScope
public class Role implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "role")
    private String role;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    private Role(){}

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        StringBuilder buildUsers = new StringBuilder();
        users.forEach((user) -> buildUsers.append(user.getName()).append(","));
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", users=[" + buildUsers.toString() +
                "]}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        return getId() == role.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
