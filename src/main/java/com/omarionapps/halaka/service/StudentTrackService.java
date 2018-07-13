package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Activity;
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

	public int getTotalActiveStudents (){
		return findAllByStatus(StudentStatus.STUDYING).size() +
				       findAllByStatus(StudentStatus.TEMP_STOP).size();
	}

	public double getActiveStudentsRate(int days){
		return getRateByStatus(StudentStatus.STUDYING, days) +
				       getRateByStatus(StudentStatus.TEMP_STOP, days);
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

	public double getRateByStatusAndActivity(StudentStatus status, int days, Activity activity) {
		LocalDate startDate = LocalDate.now().minusDays(days);
		LocalDate endDate   = LocalDate.now();

		Set<StudentTrack> statusTracks = findAllByOrderByRegisterDate()
				                                 .stream()
				                                 .filter(track -> track.getCourse().getActivity().equals(activity))
				                                 .collect(Collectors.toSet());
		Set<StudentTrack> dateFilterStatusTracks = findAllByRegisterDateBetweenAndStatus(startDate, endDate, status)
				                                           .stream()
				                                           .filter(track -> track.getCourse().getActivity().equals(activity))
				                                           .collect(Collectors.toSet());

		double            totalTracks            = statusTracks.size();
		double            filterTracks           = dateFilterStatusTracks.size();

		double rate = (filterTracks / totalTracks) * 100;
		return rate;
	}

	public Optional<StudentTrack> findById(int id) {
		return studentTrackRepository.findById(id);
	}

	public Iterable<StudentTrack> saveAll(Iterable<StudentTrack> tracks) {
		return studentTrackRepository.saveAll(tracks);
	}

	public Set<StudentTrack> findAllByStatus(StudentStatus status) {
		return studentTrackRepository.findAllByStatus(status.toString());
	}

	public Set<StudentTrack> findAllByStatusAndActivity(StudentStatus status, Activity activity) {
		return studentTrackRepository
				       .findAllByStatus(status.toString())
				       .stream()
				       .filter(track -> track.getCourse().getActivity().equals(activity))
				       .collect(Collectors.toSet());
	}

	public Set<StudentTrack> findAllByRegisterDateBetween(LocalDate start, LocalDate end) {
		Date startDate = Utils.convertLocalDate(start);
		Date endDate   = Utils.convertLocalDate(end);
		return studentTrackRepository.findAllByRegisterDateBetween(startDate, endDate);
	}
	
	public Set<StudentTrack> findAllByOrderByRegisterDate() {
		return studentTrackRepository.findAllByOrderByRegisterDate();
	}
	

	public Set<StudentTrack> findAllByRegisterDateBetweenAndStatus(LocalDate start, LocalDate end, StudentStatus
			                                                                                               status) {
		Date startDate = Utils.convertLocalDate(start);
		Date endDate   = Utils.convertLocalDate(end);
		return studentTrackRepository.findAllByRegisterDateBetweenAndStatus(startDate, endDate, status.toString());
	}
	
	public StudentTrack save(StudentTrack studentTrack) {
		return studentTrackRepository.save(studentTrack);
	}

	public void deleteById(int id) {
		studentTrackRepository.deleteById(id);
	}

}
