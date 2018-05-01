package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.*;
import com.omarionapps.halaka.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omar on 09-Apr-17.
 */
@Controller
@PropertySource(ignoreResourceNotFound = true, value = "classpath:resources_photos.properties")
public class StudentController {
	Logger logger = LoggerFactory.getLogger(StudentController.class);
	@Value("${student_photos_path}")
	String studentPhotosPath;
	private StudentService  studentService;
	private ActivityService activityService;
	private CourseService   courseService;
	private CountryService  countryService;
	private HouseService    houseService;

	@Autowired
	public StudentController(StudentService studentService, ActivityService activityService, CourseService courseService,
	                         HouseService houseService, CountryService countryService) {
		this.studentService = studentService;
		this.activityService = activityService;
		this.courseService = courseService;
		this.houseService = houseService;
		this.countryService = countryService;
	}

	/**
	 * Get all students to the list page
	 *
	 * @return ModelAndView page "student-list" with all students as internal object.
	 */
	@GetMapping("/admin/students")
	public ModelAndView getStudentsList() {
		ModelAndView model = new ModelAndView("admin/student-list");

		model.addObject("students", studentService.getAll());

		return model;
	}

	@GetMapping("/admin/students/student/{id}")
	public ModelAndView processStudentRequest(@PathVariable(value = "id") Integer id) {
		ModelAndView modelAndView;
		Student      student = studentService.getById(id);
		if (null == student) {
			modelAndView = new ModelAndView("admin/students-list");
			modelAndView.addObject("message", "There is no student with ID (" + id + ")...");
			return modelAndView;
		} else {
			modelAndView = new ModelAndView("admin/student-profile");
			prepareModel(modelAndView, student);
			return modelAndView;
		}
	}

	private void prepareModel(ModelAndView model, Student student) {
		//create the student and the registering student objects
		RegisteringStudent regStudent = new RegisteringStudent();
		//create the list of wishes
		List<Wish> wishes = new ArrayList<>();

		if (null == student) {
			//student.setStudentTracks(tracks);
			student = new Student();
			regStudent.setStudent(student);
			for (int i = 0; i < 7; i++) {
				//tracks.add(new StudentTrack());
				wishes.add(new Wish());
			}

		} else {
			regStudent.setId(student.getId());
			regStudent.setStudent(student);

			student.getStudentTracks().forEach((track -> {
				Wish wish = new Wish();
				wish.setTrack(track);
				wish.setSelected(true);
				wish.setActivityId(track.getCourse().getActivity().getId());
				House house = houseService.findByName(track.getCourse().getActivity().getName());
				wish.setHouse(house);
				wish.setActivityName(track.getActivityName());
				wish.setCertificate(track.getCertificate());
				wish.setComments(track.getComments());

				wishes.add(wish);
			}));
			System.out.println("Wishes before: " + wishes);
			for (int i = 0; i < (7 - wishes.size()); i++) {
				wishes.add(new Wish());
			}
			System.out.println("Wishes after: " + wishes);
		}

		//create the list of paths'tracks and course's tracks.
		//List<StudentTrack> tracks = new ArrayList<>();
		//List<CourseTrack> courseTracks = new ArrayList<>();
		//get the list of activities and houses from the DB
		Iterable<Activity> itrActivities = activityService.findAllOrderByName();
		Iterable<House>    itrHouses     = houseService.findAllOrderById();
		//create the list object for the activities to be sent to the view.
		List<Activity> activities = new ArrayList<>();
		List<House>    houses     = new ArrayList<>();
		//fill activities and houses lists with data from DB
		itrActivities.forEach((activity -> activities.add(activity)));
		itrHouses.forEach((house) -> houses.add(house));
		regStudent.setWishes(wishes);
		//System.out.println("ActivityCourses: " + activityService.getActivityCourses());
		//model.addObject("student", student);
		//send object of the registering student to the view to be filled
		model.addObject("regStudent", regStudent);
		//send the list of activities to the view.
		model.addObject("activities", itrActivities);
		//send the houses list to the view.
		model.addObject("houses", itrHouses);
		//send the status values to the view
		model.addObject("trackStatuses", StudentStatus.values());
		//send the countries to the view.
		model.addObject("countries", countryService.findAllByOrderByArabicNameAsc());
		model.addObject("studentPhotoPath", studentPhotosPath);

	}

	@GetMapping("/admin/students/student")
	public ModelAndView processRegisterStudentRequest() {
		return addStudent();
	}

	/**
	 * Create model for add course page
	 *
	 * @return model of page with its attributes
	 */
	private ModelAndView addStudent() {
		//set the model view.
		ModelAndView model = new ModelAndView("admin/register-student");
		prepareModel(model, null);
		return model;
	}

	@PutMapping("/admin/students/student/id")
	public String updateStudent(@Valid RegisteringStudent regStudent, @RequestParam("photo") MultipartFile photo, BindingResult bindingResult, Model model) {

		model.addAttribute("regStudent", regStudent);
		return save(regStudent, photo, bindingResult);
	}

	private String save(RegisteringStudent regStudent, MultipartFile photo, BindingResult bindingResult) {
		List<StudentTrack> tracks = new ArrayList<>();

		if (bindingResult.hasErrors()) {
			return (regStudent.getId() == 0) ? "admin/register-student" : "redirect:/admin/students/student/" + regStudent.getStudent().getId();

		} else {
			Student returnedStudent = null;
			if (regStudent.getWishes() != null) {
				System.out.println("Not Null Wish...");
				regStudent.getWishes().stream().filter((wish) -> wish.isSelected())
						.forEach((wish) -> {
							System.out.println("Wish: " + wish.toString());

							if (wish.getActivityId() == 7) {
								System.out.println("Choose housing...");
								regStudent.getStudent().setHouse(wish.getHouse());
							} else {
								StudentTrack track = new StudentTrack();
								track.setStudent(regStudent.getStudent());
								track.setRegisterDate(wish.getRegisterDate());
								track.setCertificate(wish.getCertificate());
								track.setCourse(wish.getCourse());
								track.setComments(wish.getComments());
								track.setEvaluation(wish.getEvaluation());
								track.setStartDate(wish.getStartDate());
								track.setStatus(wish.getStatus());

								tracks.add(track);
								regStudent.getStudent().setStudentTracks(tracks);
							}

						});

			} else {
				List<Wish> wishes = new ArrayList<>();
				regStudent.getStudent().getStudentTracks().stream()
						.forEach((track) -> {
							Wish wish = new Wish();
							wish.setActivityId(track.getCourse().getActivity().getId());
							wish.setSelected(true);
							wish.setTrack(track);

							wishes.add(wish);
						});
				//regStudent.getStudent().setPhoto(regStudent.getPhoto().getOriginalFilename());
				regStudent.setWishes(wishes);
			}
			System.out.println("Student: " + regStudent.getStudent());
			if (null == photo) {
				System.out.println("file is null");
				regStudent.getStudent().setPhoto(regStudent.getStudent().getPhoto());
			}
			try {
				System.out.println("Photo Name: " + photo.getOriginalFilename());
				photo.transferTo(new File(studentPhotosPath + photo.getOriginalFilename()));
				regStudent.getStudent().setPhoto(photo.getOriginalFilename());
			} catch (IOException e) {
				System.out.println(e.toString());
			}
			returnedStudent = studentService.save(regStudent.getStudent());
			return "redirect:/admin/students/student/" + returnedStudent.getId();
		}
	}

	/**
	 * Process the get request of archive operation
	 *
	 * @param id            the student id to be archived
	 * @param redirectAttrs to send message of success or failure of the operation
	 * @return redirect to the same page of courses list with new info and archive operation result message
	 */
	@GetMapping("/admin/students/student/{id}/archive")
	public String archiveStudent(@PathVariable(value = "id") int id, RedirectAttributes redirectAttrs) {
		Student student = studentService.getById(id);
		student.setArchived(true);
		student.setArchivedDate(Date.valueOf(LocalDate.now()));
		Student archivedStudent = studentService.save(student);
		if (null != archivedStudent) {
			redirectAttrs.addFlashAttribute("message", "Student with ID( " + id + " ) was archived successfully");
		} else {
			redirectAttrs.addFlashAttribute("message", "An error happens while archiving the student with ID( " +
					id + " )");
		}

		return "redirect:/admin/students";
	}

	/**
	 * Process the post request of updating existing student or creating new one
	 *
	 * @param regStudent the student to be updated or created
	 * @return redirect to the saved student profile page
	 */
	@PostMapping("/admin/students/student")
	public String registerStudent(@Valid RegisteringStudent regStudent, @RequestParam("photo") MultipartFile photo, BindingResult bindingResult, Model model) {

		return save(regStudent, photo, bindingResult);

	}

}
