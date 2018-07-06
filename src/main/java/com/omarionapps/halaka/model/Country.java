package com.omarionapps.halaka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.omarionapps.halaka.utils.StudentStatus;

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
public class Country implements Serializable, Comparable<Country> {
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

	@JsonIgnore
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

	/**
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.
	 * <p>
	 * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)
	 * <p>
	 * <p>The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
	 * <tt>x.compareTo(z)&gt;0</tt>.
	 * <p>
	 * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
	 * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
	 * all <tt>z</tt>.
	 * <p>
	 * <p>It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
	 * class that implements the <tt>Comparable</tt> interface and violates
	 * this condition should clearly indicate this fact.  The recommended
	 * language is "Note: this class has a natural ordering that is
	 * inconsistent with equals."
	 * <p>
	 * <p>In the foregoing description, the notation
	 * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
	 * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
	 * <tt>0</tt>, or <tt>1</tt> according to whether the value of
	 * <i>expression</i> is negative, zero or positive.
	 *
	 * @param country the country to be compared.
	 * @return a negative integer, zero, or a positive integer as this object
	 * is less than, equal to, or greater than the specified object.
	 * @throws NullPointerException if the specified object is null
	 * @throws ClassCastException   if the specified object's type prevents it
	 *                              from being compared to this object.
	 */
	@Override
	public int compareTo(Country country) {
		int currentStudentSize = this.getStudents().size();
		int otherStudentSize   = country.getStudents().size();

		if (currentStudentSize > otherStudentSize)
			return 1;

		if (currentStudentSize == otherStudentSize)
			return 0;

		return -1;
	}
}
