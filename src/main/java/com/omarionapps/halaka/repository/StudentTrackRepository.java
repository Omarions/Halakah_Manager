package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.StudentTrack;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Omar on 02-Aug-17.
 */
@Repository
public interface StudentTrackRepository extends CrudRepository<StudentTrack, Integer> {
    StudentTrack save(StudentTrack studentTrack);
}
