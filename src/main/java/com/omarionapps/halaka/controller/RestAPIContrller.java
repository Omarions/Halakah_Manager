package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Activity;
import com.omarionapps.halaka.model.Course;
import com.omarionapps.halaka.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by Omar on 20-Jul-17.
 */
@RestController
public class RestAPIContrller {
    @Autowired
    private ActivityService activityService;

    @GetMapping("admin/students/student/activity/{id}/courses")
    public ResponseEntity<Iterable<Course>> getActivityCourses(@PathVariable(value = "id") int id) {
	    Optional<Activity>               optActivity = activityService.findById(id);
	    Activity                         activity    = optActivity.get();
	    Iterable<Course>                 courses     = activity.getCourses();
        ResponseEntity<Iterable<Course>> re          = new ResponseEntity(courses, HttpStatus.OK);
        System.out.println("JS: " + re.toString());
        return re;
    }
}
