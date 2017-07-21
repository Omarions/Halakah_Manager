package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Course;
import com.omarionapps.halaka.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Omar on 20-Jul-17.
 */
@RestController
public class RestAPIContrller {
    @Autowired
    private ActivityService activityService;

    @GetMapping("admin/students/student/activity/{id}/courses")
    public ResponseEntity<Iterable<Course>> getActivityCourses(@PathVariable(value = "id") int id) {
        Iterable<Course> courses = activityService.findById(id).getCourses();
        ResponseEntity<Iterable<Course>> re = new ResponseEntity(courses, HttpStatus.OK);
        System.out.println("JS: " + re.toString());
        return re;
    }
}
