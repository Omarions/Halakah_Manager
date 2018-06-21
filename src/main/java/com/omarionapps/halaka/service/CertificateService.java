package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Certificate;
import com.omarionapps.halaka.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<Certificate> findById(int id) {
	    return certificateRepository.findById(id);
    }

    /**
     * Get the count of certificates
     */
    public long getCount() {
	    return certificateRepository.count();
    }

    /**
     * Get All certificates
     */
    public Iterable<Certificate> findAll() {
        return certificateRepository.findAll();
    }

	/**
	 * Get All certificates
	 */
	public List<Certificate> findAllByOrderById() {
		return certificateRepository.findAllByOrderById();
	}
	/**
	 * Save new or update certificate
	 */
	public Certificate save(Certificate certificate) {
		return certificateRepository.save(certificate);
	}

	public void delete(int certId) {
		certificateRepository.deleteById(certId);
	}
}
