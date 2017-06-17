package com.omarionapps.halaka.service;

import com.omarionapps.halaka.repository.CourseTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Omar on 03-May-17.
 */
@Service
public class CourseTrackService {
    private CourseTrackRepository courseTrackRepository;

    @Autowired
    public CourseTrackService(CourseTrackRepository courseTrackRepository){
        this.courseTrackRepository = courseTrackRepository;
    }
}
