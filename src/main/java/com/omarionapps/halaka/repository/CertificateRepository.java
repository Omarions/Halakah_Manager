package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.Certificate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Omar on 07-Jul-17.
 */
@Repository
public interface CertificateRepository extends CrudRepository<Certificate, Integer> {

}
