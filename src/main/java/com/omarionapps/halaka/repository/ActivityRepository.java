package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Omar on 29-Apr-17.
 */
@Repository("activityRepository")
public interface ActivityRepository extends CrudRepository<Activity, Integer> {
    Iterable<Activity> findAllByOrderByName();
}
