package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Omar on 09-Apr-17.
 */
@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {

	Iterable<Student> findAllByOrderByCountry();


}
