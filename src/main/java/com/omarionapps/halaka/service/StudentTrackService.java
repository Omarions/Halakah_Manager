package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.StudentTrack;
import com.omarionapps.halaka.repository.StudentTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public StudentTrack save(StudentTrack studentTrack) {
        return studentTrackRepository.save(studentTrack);
    }
}
