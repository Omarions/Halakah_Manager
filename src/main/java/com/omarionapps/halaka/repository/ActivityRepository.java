package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Omar on 29-Apr-17.
 */
@Repository
public interface ActivityRepository extends CrudRepository<Activity, Integer> {
	List<Activity> findAllByOrderByName();
}
