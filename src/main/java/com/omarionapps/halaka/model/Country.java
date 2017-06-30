package com.omarionapps.halaka.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Omar on 08-May-17.
 */
@Entity
public class Country implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String arabicName;
    private String englishName;
    private String code;
    @OneToMany(mappedBy = "country")
    private Set<Student> students = new HashSet<>();
    @Transient
    private List<Certificate> certificates = new ArrayList<>();

    public Country(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArabicName() {
        return arabicName;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public List<Certificate> getCertificates() {
        certificates.clear();
        students.forEach((s) -> {
            s.getStudentTracks().stream()
                    .filter((st) -> st.getStudent() == s && st.getStatus().equalsIgnoreCase(StudentStatus.CERTIFIED.name()))
                    .forEach((st) -> certificates.add(st.getCertificate()));
        });
        return certificates;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", arabicName='" + arabicName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", code='" + code + '\'' + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;

        Country country = (Country) o;

        return getId() == country.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
