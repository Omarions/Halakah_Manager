package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.Activity;
import com.omarionapps.halaka.model.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Omar on 30-Apr-17.
 */
@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {
	List<Course> findAllByOrderByName();

	List<Course> findCoursesByActivity_IdAndDaysIsContaining(int activityId, String day);

	List<Course> findCoursesByActivity(Activity activity);
}
