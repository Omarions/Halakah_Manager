package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Omar on 30-Apr-17.
 */
@Repository("courseRepository")
public interface CourseRepository extends CrudRepository<Course, Integer> {
    Iterable<Course> findAllByOrderByName();
}
