package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.StudentStatus;
import com.omarionapps.halaka.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Omar on 09-Apr-17.
 */
@Service
public class StudentService {
    long countByStatus = 0;
    private StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public Student getById(Integer id) {
        return studentRepository.findOne(id);
    }

    public Iterable<Student> getAll (){ return studentRepository.findAll(); }

    public Iterable<Student> findAllOrderByCountry(){ return studentRepository.findAllByOrderByCountry(); }

    public int getCountWaitingStudentsByCourse(int studentId, int courseId){
        return -1;
    }

    public long getCountByStatus(StudentStatus status){
        countByStatus = 0;
        Iterable<Student> students = this.getAll();
        students.forEach((student) -> {
            countByStatus += student.getStudentTracks().stream().filter((studentTrack -> studentTrack.getStatus().equalsIgnoreCase(status.toString()))).count();
        });
        return countByStatus;
    }

    public Student save(Student student) {

        return studentRepository.save(student);

    }

    public void delete(int id) {
        studentRepository.delete(id);
    }
}
