package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.*;
import com.omarionapps.halaka.service.*;
import com.omarionapps.halaka.utils.LocationTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Omar on 09-Apr-17.
 */
@Controller
@PropertySource(ignoreResourceNotFound = true, value = "classpath:resources_photos.properties")
public class StudentController {
	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Value("C:\\halaka\\resources\\images\\")
	String studentPhotosPath;
	private StudentService  studentService;
	private ActivityService activityService;
	private CourseService   courseService;
	private CountryService  countryService;
	private HouseService    houseService;
	private StorageService  storageService;

	@Autowired
	public StudentController(StudentService studentService, ActivityService activityService, CourseService courseService,
	                         HouseService houseService, CountryService countryService, StorageService storageService) {
		this.studentService = studentService;
		this.activityService = activityService;
		this.courseService = courseService;
		this.houseService = houseService;
		this.countryService = countryService;
		this.storageService = storageService;
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
		ModelAndView      modelAndView;
		Optional<Student> optStudent = studentService.getById(id);
		Student           student    = optStudent.get();
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

	private void prepareModel(ModelAndView model, Student student) {
		//create the list object for the activities to be sent to the view.
		List<Activity> activities = new ArrayList<>();
		List<House>    houses     = new ArrayList<>();
		String imagePath = null;
		//create the student and the registering student objects
		RegisteringStudent regStudent = new RegisteringStudent();

		//create the list of wishes
		List<Wish> wishes = new ArrayList<>();

		if (null == student) {
			//student.setStudentTracks(tracks);
			student = new Student();
			for (int i = 0; i < 7; i++) {
				//tracks.add(new StudentTrack());
				wishes.add(new Wish());
			}

		} else {
			regStudent.setId(student.getId());
			regStudent.setName(student.getName());
			regStudent.setBirthDate(student.getBirthDate());
			regStudent.setBirthLocation(student.getBirthLocation());
			regStudent.setCountry(student.getCountry());
			regStudent.setComments(student.getComments());
			regStudent.setEducation(student.getEducation());
			regStudent.setIdentityId(student.getIdentityId());
			regStudent.setJob(student.getJob());
			regStudent.setTel(student.getTel());
			regStudent.setEgyptAddress(student.getEgyptAddress());
			regStudent.setHomeAddress(student.getHomeAddress());
			regStudent.setFacebook(student.getFacebook());
			regStudent.setGender(student.getGender());
			regStudent.setEmail(student.getEmail());
			//regStudent.setPhoto(student.getPhoto());

			imagePath = MvcUriComponentsBuilder
					.fromMethodName(StudentController.class, "getFile", student.getPhoto()).build().toString();

			System.out.println("Image Path: " + imagePath);

			student.getStudentTracks().forEach((track -> {
				Wish wish = new Wish();
				wish.setTrack(track);
				wish.setSelected(true);
				wish.setActivityId(track.getCourse().getActivity().getId());
				House house = houseService.findByName(track.getCourse().getActivity().getName());
				wish.setHouse(house);
				wishes.add(wish);
			}));
		}
		regStudent.setWishes(wishes);

		//get the list of activities and houses from the DB
		Iterable<Activity> itrActivities = activityService.findAllOrderByName();
		Iterable<House>    itrHouses     = houseService.findAllOrderById();
		//fill activities and houses lists with data from DB
		itrActivities.forEach((activity -> activities.add(activity)));
		itrHouses.forEach((house) -> houses.add(house));

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
		model.addObject("imagePath", imagePath);

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
		Optional<Student> optStudent = studentService.getById(id);
		Student           student    = optStudent.get();
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

	@PostMapping("/admin/students/student/{id}")
	public String updateStudent(@Valid RegisteringStudent regStudent, @RequestParam("image") MultipartFile image, @PathVariable Integer id, BindingResult bindingResult) {
		Student student = studentService.getById(id).get();
		student.setName(regStudent.getName());
		student.setBirthDate(regStudent.getBirthDate());
		student.setArchived(false);
		student.setEmail(regStudent.getEmail());
		student.setFacebook(regStudent.getFacebook());
		student.setGender(regStudent.getGender());
		student.setHomeAddress(regStudent.getHomeAddress());
		student.setEgyptAddress(regStudent.getEgyptAddress());
		student.setEducation(regStudent.getEducation());
		student.setCountry(regStudent.getCountry());
		student.setBirthLocation(regStudent.getBirthLocation());
		regStudent.getWishes().forEach(wish ->
				student.getStudentTracks().add(wish.getTrack())
		);
		if (null == image) {
			System.out.println("file is null");
			student.setPhoto(student.getPhoto());
		} else {
			try {
				System.out.println("New Image: " + image.getOriginalFilename());
				storageService.store(image, LocationTag.STUDENTS_STORE_LOC);
				System.out.println("Old Student Photo: " + student.getPhoto());
				storageService.deletePhotoByName(student.getPhoto(), LocationTag.STUDENTS_STORE_LOC);
				student.setPhoto(image.getOriginalFilename());
			} catch (Exception e) {
				log.error("Fails to Store the image!");
				log.error(e.toString());
			}
			/*
			try {
				String path = image.getOriginalFilename();
				System.out.println("Path: " + path);
				image.transferTo(new File(studentPhotosPath + path));
				student.setPhoto(image.getOriginalFilename());
			} catch (IOException e) {
				System.out.println(e.toString());
			}
			*/
		}
		Student returnedStudent = studentService.save(student);
		return "redirect:/admin/students/student/" + returnedStudent.getId();
	}

	/**
	 * Process the post request of updating existing student or creating new one
	 *
	 * @param regStudent the student to be updated or created
	 * @return redirect to the saved student profile page
	 */
	@PostMapping("/admin/students/student")
	public String registerStudent(@Valid RegisteringStudent regStudent, @RequestParam("photo") MultipartFile image,
	                              BindingResult bindingResult, Model model) {

		return save(regStudent, image, bindingResult);

	}

	private String save(RegisteringStudent regStudent, MultipartFile photo, BindingResult bindingResult) {
		List<StudentTrack> tracks = new ArrayList<>();

		if (bindingResult.hasErrors()) {
			return (regStudent.getId() == 0) ? "admin/register-student" : "redirect:/admin/students/student/" + regStudent.getId();

		} else {
			Student returnedStudent = null;
			if (regStudent.getWishes() != null) {
				System.out.println("Not Null Wish...");
				regStudent.getWishes().stream().filter((wish) -> wish.isSelected())
						.forEach((wish) -> {
							System.out.println("Wish: " + wish.toString());

							if (wish.getActivityId() == 7) {
								System.out.println("Choose housing...");
								regStudent.setHouse(wish.getHouse());
							} else {
								StudentTrack track = new StudentTrack();
								track.setStudent(regStudent);
								track.setRegisterDate(wish.getTrack().getRegisterDate());
								track.setCertificate(wish.getTrack().getCertificate());
								track.setCourse(wish.getTrack().getCourse());
								track.setComments(wish.getTrack().getComments());
								track.setEvaluation(wish.getTrack().getEvaluation());
								track.setStartDate(wish.getTrack().getStartDate());
								track.setStatus(wish.getTrack().getStatus());

								tracks.add(track);
								regStudent.setStudentTracks(tracks);
							}

						});

			} else {
				List<Wish> wishes = new ArrayList<>();
				regStudent.getStudentTracks().stream()
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
			System.out.println("Student: " + regStudent);
			if (null == photo) {
				System.out.println("file is null");
				regStudent.setPhoto(regStudent.getPhoto());
			}
			try {
				System.out.println("Photo Name: " + photo.getOriginalFilename());
				photo.transferTo(new File("/images/" + photo.getOriginalFilename()));
				regStudent.setPhoto(photo.getOriginalFilename());
			} catch (IOException e) {
				System.out.println(e.toString());
			}
			returnedStudent = studentService.save(regStudent);
			return "redirect:/admin/students/student/" + returnedStudent.getId();
		}
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.loadFile(filename, LocationTag.STUDENTS_STORE_LOC);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
