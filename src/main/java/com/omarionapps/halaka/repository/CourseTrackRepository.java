package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.CourseTrack;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Omar on 03-May-17.
 */
@Repository
public interface CourseTrackRepository extends CrudRepository<CourseTrack, Integer> {
}
