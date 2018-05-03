package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Teacher;
import com.omarionapps.halaka.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Omar on 30-Apr-17.
 */
@Service
public class TeacherService {
    private TeacherRepository teacherRepository;
    private long totalActivityCourses = 0;
    private long totalTeachers = 0;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository){
        this.teacherRepository = teacherRepository;
    }

    public Iterable<Teacher> findAllByOrderByName() {
        Iterable<Teacher> teachers = teacherRepository.findAllByOrderByName();
        return teachers;
    }

    public Iterable<Teacher> findAllByOrderByID() {
        Iterable<Teacher> teachers = teacherRepository.findAllByOrderById();
        return teachers;
    }

    public long getCount(){
        totalTeachers = 0;
        this.findAll().forEach(teacher -> ++totalTeachers);
        return totalTeachers;
    }

    public Iterable<Teacher> findAll() {
        return teacherRepository.findAllByOrderByName();
    }

	public Optional<Teacher> findOneById(int id) {
		return teacherRepository.findById(id);
    }

    public Iterable<Teacher> findAllByActivity(int activityId){
        return teacherRepository.findAllByActivityId(activityId);
    }

    public long getCountByArchived(boolean isArchived) {
        return findAllByArchive(isArchived).size();
    }

    public Set<Teacher> findAllByArchive(boolean isArchived) {
        Set<Teacher> archivedTeachers = new HashSet<>();
        Iterable<Teacher> teachers = findAll();
        teachers.forEach((teacher) -> {
            if (teacher.isArchived() == isArchived)
                archivedTeachers.add(teacher);
        });

        return archivedTeachers;
    }

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
}
