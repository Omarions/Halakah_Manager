package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.House;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Omar on 15-Apr-17.
 */
@Repository
public interface HouseRepository extends CrudRepository<House, Integer>{

}
