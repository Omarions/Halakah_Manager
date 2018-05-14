package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.StudentTrack;
import com.omarionapps.halaka.service.StudentTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class StudentTrackController {
	private StudentTrackService studentTrackService;

	@Autowired
	public StudentTrackController(StudentTrackService studentTrackService) {
		this.studentTrackService = studentTrackService;
	}

	@GetMapping("/admin/student-tracks/track/{trackId}")
	public String removeTrack(@PathVariable(value = "trackId") Integer trackId, @RequestParam(value = "remove", required = false) boolean remove) {

		Optional<StudentTrack> foundTrack = studentTrackService.findById(trackId);
		Student                student    = foundTrack.get().getStudent();

		studentTrackService.deleteById(trackId);

		return "redirect:/admin/students/student/" + student.getId();
	}
}
