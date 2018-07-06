package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.StudentTrack;
import com.omarionapps.halaka.repository.StudentTrackRepository;
import com.omarionapps.halaka.utils.StudentStatus;
import com.omarionapps.halaka.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Omar on 02-Aug-17.
 */
@Service
public class StudentTrackService {
    private StudentTrackRepository studentTrackRepository;

	@Autowired
    public StudentTrackService(StudentTrackRepository studentTrackRepository) {
        this.studentTrackRepository = studentTrackRepository;
    }

	public Optional<StudentTrack> findById(int id) {
		return studentTrackRepository.findById(id);
	}

	public Set<StudentTrack> findAllByStatus(StudentStatus status) {
		return studentTrackRepository.findAllByStatus(status.toString());
	}

	public Set<StudentTrack> findAllByRegisterDateBetween(LocalDate start, LocalDate end) {
		Date startDate = Utils.convertLocalDate(start);
		Date endDate   = Utils.convertLocalDate(end);
		return studentTrackRepository.findAllByRegisterDateBetween(startDate, endDate);
	}

	public double getRateByStatus(StudentStatus status, int days) {
		LocalDate startDate = LocalDate.now().minusDays(days);
		LocalDate endDate   = LocalDate.now();

		Set<StudentTrack> statusTracks = findAllByOrderByRegisterDate()
				                                 .stream()
				                                 .collect(Collectors.toSet());
		Set<StudentTrack> dateFilterStatusTracks = findAllByRegisterDateBetweenAndStatus(startDate, endDate, status);
		double            totalTracks            = statusTracks.size();
		double            filterTracks           = dateFilterStatusTracks.size();

		double rate = (filterTracks / totalTracks) * 100;
		return rate;
	}

	public Set<StudentTrack> findAllByOrderByRegisterDate() {
		return studentTrackRepository.findAllByOrderByRegisterDate();
	}
    public StudentTrack save(StudentTrack studentTrack) {
        return studentTrackRepository.save(studentTrack);
    }

    public Iterable<StudentTrack> saveAll(Iterable<StudentTrack> tracks) {
        return studentTrackRepository.saveAll(tracks);
    }

    public void deleteById(int id) {
        studentTrackRepository.deleteById(id);
    }

	public Set<StudentTrack> findAllByRegisterDateBetweenAndStatus(LocalDate start, LocalDate end, StudentStatus
			                                                                                               status) {
		Date startDate = Utils.convertLocalDate(start);
		Date endDate   = Utils.convertLocalDate(end);
		return studentTrackRepository.findAllByRegisterDateBetweenAndStatus(startDate, endDate, status.toString());
	}
}
