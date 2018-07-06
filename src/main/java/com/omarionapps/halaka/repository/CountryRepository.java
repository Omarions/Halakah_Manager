package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Omar on 08-May-17.
 */
@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {
    List<Country> findAllByOrderByEnglishNameAsc();

	List<Country> findAllByOrderByArabicNameAsc();
}
