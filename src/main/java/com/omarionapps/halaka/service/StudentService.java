package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Activity;
import com.omarionapps.halaka.model.RegisteringStudent;
import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.StudentTrack;
import com.omarionapps.halaka.repository.StudentRepository;
import com.omarionapps.halaka.utils.StudentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Omar on 09-Apr-17.
 */
@Service
public class StudentService {
	long countByStatus = 0;
	long totalStudents;
	private StudentRepository   studentRepository;
	private StudentTrackService studentTrackService;

	@Autowired
	public StudentService(StudentRepository studentRepository, StudentTrackService studentTrackService) {
		this.studentRepository = studentRepository;
		this.studentTrackService = studentTrackService;
	}

	public Optional<Student> findById(Integer id) {
		return studentRepository.findById(id);
	}

	public Iterable<Student> findAllOrderByCountry() {
		return studentRepository.findAllByOrderByCountry();
	}

	public long getTotalStudents() {
		totalStudents = 0;
		Iterable<Student> students = this.findAll();
		students.forEach(student -> {
			totalStudents++;
		});
		return totalStudents;
	}

	public double getStatusPercentage(StudentStatus status){
		return (Double.valueOf(getCountByStatus(status)) /
				        Double.valueOf(getTotalStudents())) * 100;
	}

	public long getCountByStatus(StudentStatus status, boolean isArchived) {
		countByStatus = 0;
		Iterable<Student> students = this.findAll();

		students.forEach((student) -> {
			for (StudentTrack st : student.getStudentTracks()) {
				if (st.getStatus().equalsIgnoreCase(status.name()) && student.isArchived() == isArchived)
					countByStatus++;
			}
		});
		return countByStatus;
	}

	public long getCountByStatus(StudentStatus status) {
	/*	System.out.println("Distinct Students: ");
		studentTrackService.findAllByStatus(status).stream().map(track -> track.getStudent()).distinct().forEach
				                                                                                                 (System.out::println);
		System.out.println("Distinct Students count: ");
		System.out.println(studentTrackService.findAllByStatus(status).stream().map(track -> track.getStudent()).distinct().count());*/
		return studentTrackService.findAllByStatus(status).stream().map(track -> track.getStudent()).distinct()
		                          .count();
	}

	public long getCountByStatusAndActivity(StudentStatus status, Activity activity) {
	/*	System.out.println("Distinct Students: ");
		studentTrackService.findAllByStatus(status).stream().map(track -> track.getStudent()).distinct().forEach
				                                                                                                 (System.out::println);
		System.out.println("Distinct Students count: ");
		System.out.println(studentTrackService.findAllByStatus(status).stream().map(track -> track.getStudent()).distinct().count());*/
		return studentTrackService.findAllByStatus(status)
		                          .stream()
		                          .filter(track -> track.getCourse().getActivity().equals(activity))
		                          .map(track -> track.getStudent())
		                          .distinct()
		                          .count();
	}

	public Student registerStudent(RegisteringStudent regStudent) {
		Student student = new Student();
		student.setName(regStudent.getName());
		student.setGender(regStudent.getGender());
		student.setIdentityId(regStudent.getIdentityId());
		student.setBirthDate(regStudent.getBirthDate());
		student.setBirthLocation(regStudent.getBirthLocation());
		student.setCountry(regStudent.getCountry());
		student.setHomeAddress(regStudent.getHomeAddress());
		student.setEgyptAddress(regStudent.getEgyptAddress());
		student.setTel(regStudent.getTel());
		student.setEmail(regStudent.getEmail());
		student.setFacebook(regStudent.getFacebook());
		student.setEducation(regStudent.getEducation());
		student.setJob(regStudent.getJob());
		student.setComments(regStudent.getComments());
		student.setPhoto(regStudent.getPhoto());
		student.setArchived(false);
		student.setArchivedDate(null);
		student.setStudentTracks(regStudent.getStudentTracks());
		//System.out.println("Student after cast: " + student);
		return save(student);
	}

	public Student save(Student student) {

		Student returnedStudent = studentRepository.save(student);
		for (StudentTrack st : student.getStudentTracks()) {
			if (st != null) {
				st.setStudent(returnedStudent);
				studentTrackService.save(st);
			}
		}
		return returnedStudent;
	}

	public Student updateStudent(Student student) {

		Student returnedStudent = studentRepository.save(student);
		/*
		for (StudentTrack st : student.getStudentTracks()) {
			if (st != null) {
				Optional<StudentTrack> track = studentTrackService.findById(st.getId());
				if(track.isPresent()){
					System.out.println("Found Track: " + st);
				}else{
					st.setStudent(returnedStudent);
					System.out.println("There is new track");
					System.out.println("New Track: " + st);
					studentTrackService.save(st);
				}
			} else {
				System.out.println("No Student Track");
			}

		}
		*/
		return returnedStudent;
	}

	public void delete(int id) {
		studentRepository.deleteById(id);
	}

	public long getCountByArchived(boolean isArchived) {
		return findAllByArchive(isArchived).size();
	}

	public Set<Student> findAllByArchive(boolean isArchived) {
		Set<Student>      archivedStudents = new HashSet<>();
		Iterable<Student> students         = findAll();
		students.forEach((student) -> {
			if (student.isArchived() == isArchived)
				archivedStudents.add(student);
		});

		return archivedStudents;
	}

	private Iterable<Student> findAll() {
		return studentRepository.findAll();
	}


}
