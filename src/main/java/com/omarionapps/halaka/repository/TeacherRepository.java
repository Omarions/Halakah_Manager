package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.Course;
import com.omarionapps.halaka.model.Teacher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Omar on 30-Apr-17.
 */
@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Integer> {
    Iterable<Teacher> findAllByOrderByName();

    Iterable<Teacher> findAllByOrderById();

    Iterable<Teacher> findAllByActivityId(int activityId);

    List<Course> findCoursesByActivity(int activityId);
}
