package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by Omar on 05-Aug-17.
 */
@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {

}
