package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.StudentTrack;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

/**
 * Created by Omar on 02-Aug-17.
 */
@Repository
public interface StudentTrackRepository extends CrudRepository<StudentTrack, Integer> {
	Set<StudentTrack> findAllByRegisterDateBetween(Date start, Date end);

	Set<StudentTrack> findAllByRegisterDateBetweenAndStatus(Date start, Date end, String status);

	Set<StudentTrack> findAllByOrderByRegisterDate();

	Set<StudentTrack> findAllByStatus(String status);
}
