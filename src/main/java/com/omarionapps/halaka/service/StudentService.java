package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.RegisteringStudent;
import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.StudentStatus;
import com.omarionapps.halaka.model.StudentTrack;
import com.omarionapps.halaka.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Omar on 09-Apr-17.
 */
@Service
public class StudentService {
    long countByStatus = 0;
    long totalStudents;
    private StudentRepository studentRepository;
    private StudentTrackService studentTrackService;

    @Autowired
    public StudentService(StudentRepository studentRepository, StudentTrackService studentTrackService) {
        this.studentRepository = studentRepository;
        this.studentTrackService = studentTrackService;
    }

    public Student getById(Integer id) {
        return studentRepository.findOne(id);
    }

    public Iterable<Student> findAllOrderByCountry(){ return studentRepository.findAllByOrderByCountry(); }

    public int getCountWaitingStudentsByCourse(int studentId, int courseId){
        return -1;
    }

    public long getCountByStatus(StudentStatus status, boolean isArchived) {
        countByStatus = 0;
        Iterable<Student> students = this.getAll();

        students.forEach((student) -> {
            System.out.println("Student: " + student);
            for (StudentTrack st : student.getStudentTracks()) {
                if (st.getStatus().equalsIgnoreCase(status.name()) && student.isArchived() == isArchived)
                    countByStatus++;
            }
        });
        return countByStatus;
    }

    public Iterable<Student> getAll() {
        return studentRepository.findAll();
    }

	public Student registerStudent(RegisteringStudent student) {
        /*
        Student returnedStudent = studentRepository.save(student);
        for (StudentTrack st : student.getStudentTracks()) {
            if (st != null) {
                //System.out.println("Returned Student: " + returnedStudent);
                //System.out.println("ST: " + st);
                st.setStudent(returnedStudent);
                studentTrackService.save(st);
            } else {
                System.out.println("No Student Track");
            }

        }
        return studentRepository.save(student);
        */
		return null;
	}

    public Student save(Student student) {

        Student returnedStudent = studentRepository.save(student);
        for (StudentTrack st : student.getStudentTracks()) {
            if (st != null) {
	            //System.out.println("Returned Student: " + returnedStudent);
	            //System.out.println("ST: " + st);
                st.setStudent(returnedStudent);
                studentTrackService.save(st);
            } else {
                System.out.println("No Student Track");
            }

        }
        return studentRepository.save(student);
    }

    public void delete(int id) {
        studentRepository.delete(id);
    }

    public long getCountByArchived(boolean isArchived) {
        return findAllByArchive(isArchived).size();
    }

    public Set<Student> findAllByArchive(boolean isArchived) {
        Set<Student> archivedStudents = new HashSet<>();
        Iterable<Student> students = findAll();
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
