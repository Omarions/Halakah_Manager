package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Certificate;
import com.omarionapps.halaka.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Omar on 07-Jul-17.
 */
@Service
public class CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;
    private long count;

    /**
     * look for a certificate with its id
     *
     * @param id to look for
     * @return the certificate with the given id
     */
    public Certificate findById(long id) {
        return certificateRepository.findOne(id);
    }

    /**
     * Get the count of certificates
     */
    public long getCount() {
        count = 0;
        findAll().forEach((certificate) -> count++);

        return count;
    }

    /**
     * Get All certificates
     */
    public Iterable<Certificate> findAll() {
        return certificateRepository.findAll();
    }


}
