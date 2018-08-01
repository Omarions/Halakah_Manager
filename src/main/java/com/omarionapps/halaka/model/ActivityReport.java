package com.omarionapps.halaka.model;

public class ActivityReport {

	private Activity activity;
	private long totalActiveStudents;
	private long totalWaitingStudents;
	private long totalCertifiedStudents;
	private long totalCertificates;
	private long totalCSNationalities;
	private long totalStudentsNationalities;
	private long totalStudentTeacher;
	private long totalStudentsForST;
	private long totalFemales;
	private long totalMales;
	

	public ActivityReport() {
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public long getTotalActiveStudents() {
		return totalActiveStudents;
	}

	public void setTotalActiveStudents(long totalActiveStudents) {
		this.totalActiveStudents = totalActiveStudents;
	}

	public long getTotalWaitingStudents() {
		return totalWaitingStudents;
	}

	public void setTotalWaitingStudents(long totalWaitingStudents) {
		this.totalWaitingStudents = totalWaitingStudents;
	}

	public long getTotalCertifiedStudents() {
		return totalCertifiedStudents;
	}

	public void setTotalCertifiedStudents(long totalCertifiedStudents) {
		this.totalCertifiedStudents = totalCertifiedStudents;
	}

	public long getTotalCertificates() {
		return totalCertificates;
	}

	public void setTotalCertificates(long totalCertificates) {
		this.totalCertificates = totalCertificates;
	}

	public long getTotalCSNationalities() {
		return totalCSNationalities;
	}

	public void setTotalCSNationalities(long totalCSNationalities) {
		this.totalCSNationalities = totalCSNationalities;
	}

	public long getTotalStudentsNationalities() {
		return totalStudentsNationalities;
	}

	public void setTotalStudentsNationalities(long totalStudentsNationalities) {
		this.totalStudentsNationalities = totalStudentsNationalities;
	}

	public long getTotalStudentTeacher() {
		return totalStudentTeacher;
	}

	public void setTotalStudentTeacher(long totalStudentTeacher) {
		this.totalStudentTeacher = totalStudentTeacher;
	}

	public long getTotalStudentsForST() {
		return totalStudentsForST;
	}

	public void setTotalStudentsForST(long totalStudentsForST) {
		this.totalStudentsForST = totalStudentsForST;
	}

	public long getTotalFemales() {
		return totalFemales;
	}

	public void setTotalFemales(long totalFemales) {
		this.totalFemales = totalFemales;
	}

	public long getTotalMales() {
		return totalMales;
	}

	public void setTotalMales(long totalMales) {
		this.totalMales = totalMales;
	}

	@Override
	public String toString() {
		return "ActivityReport{" +
				       "activity=" + activity +
				       ", totalActiveStudents=" + totalActiveStudents +
				       ", totalWaitingStudents=" + totalWaitingStudents +
				       ", totalCertifiedStudents=" + totalCertifiedStudents +
				       ", totalCertificates=" + totalCertificates +
				       ", totalCSNationalities=" + totalCSNationalities +
				       ", totalStudentsNationalities=" + totalStudentsNationalities +
				       ", totalStudentTeacher=" + totalStudentTeacher +
				       ", totalStudentsForST=" + totalStudentsForST +
				       ", totalFemales=" + totalFemales +
				       ", totalMales=" + totalMales +
				       '}';
	}
}