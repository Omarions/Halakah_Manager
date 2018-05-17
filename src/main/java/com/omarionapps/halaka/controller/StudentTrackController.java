package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.StudentStatus;
import com.omarionapps.halaka.model.StudentTrack;
import com.omarionapps.halaka.service.StudentTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.Optional;

@Controller
public class StudentTrackController {
	private StudentTrackService studentTrackService;

	@Autowired
	public StudentTrackController(StudentTrackService studentTrackService) {
		this.studentTrackService = studentTrackService;
	}

	@GetMapping("/admin/student-tracks/track/{trackId}/remove")
	public String removeTrack(@PathVariable(value = "trackId") Integer trackId) {

		Optional<StudentTrack> foundTrack = studentTrackService.findById(trackId);
		Student                student    = foundTrack.get().getStudent();

		studentTrackService.deleteById(trackId);

		return "redirect:/admin/students/student/" + student.getId();
	}

	@GetMapping("/admin/student-tracks/track/{trackId}")
	public ModelAndView getUpdateTrackView(@PathVariable(value = "trackId") Integer trackId) {
		ModelAndView           modelAndView    = new ModelAndView("admin/student-tracks-form");
		Optional<StudentTrack> optStudentTrack = studentTrackService.findById(trackId);
		StudentTrack           studentTrack    = optStudentTrack.get();

		String imagePath = MvcUriComponentsBuilder
				.fromMethodName(PhotoController.class, "getFile", studentTrack.getStudent().getPhoto()).build().toString();

		modelAndView.addObject("statusEnums", StudentStatus.values());
		modelAndView.addObject("imagePath", imagePath);
		modelAndView.addObject("stTrack", studentTrack);

		return modelAndView;
	}

	@PostMapping("/admin/student-tracks/tracks")
	public String updateTrack(StudentTrack stTrack) {
		Student student = stTrack.getStudent();

		studentTrackService.save(stTrack);
		return "redirect:/admin/students/student/" + student.getId();
	}
}
