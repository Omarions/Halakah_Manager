package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.*;
import com.omarionapps.halaka.service.*;
import com.omarionapps.halaka.utils.LocationTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Omar on 09-Apr-17.
 */
@Controller
public class StudentController {
	Logger log = LoggerFactory.getLogger(this.getClass().getName());

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
	public ModelAndView getStudentsListView() {
		ModelAndView model = new ModelAndView("admin/student-list");

		model.addObject("students", studentService.getAll());

		return model;
	}

	/**
	 * Get the profile of student with specific ID
	 *
	 * @param id of student to search for
	 * @return the profile view with populated data
	 */
	@GetMapping("/admin/students/student/{id}")
	public ModelAndView getStudentProfile(@PathVariable(value = "id") Integer id) {
		ModelAndView modelAndView;
		String       imagePath = "";

		Optional<Student> optStudent = studentService.getById(id);
		Student           student    = optStudent.get();
		if (null == student) {
			modelAndView = new ModelAndView("admin/students-list");
			modelAndView.addObject("message", "There is no student with ID (" + id + ")...");
			return modelAndView;
		} else {
			modelAndView = new ModelAndView("admin/student-profile");
			//prepareModel(modelAndView, student);
			List<Wish> wishes = new ArrayList<>();
			//create the student and the registering student objects
			RegisteringStudent regStudent = new RegisteringStudent();

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
			regStudent.setPhoto(student.getPhoto());
			regStudent.setStudentTracks(student.getStudentTracks());

			student.getStudentTracks().forEach((track -> {
				Wish wish = new Wish();
				wish.setTrack(track);
				wish.setSelected(true);
				wish.setActivityId(track.getCourse().getActivity().getId());
				House house = houseService.findByName(track.getCourse().getActivity().getName());
				wish.setHouse(house);
				wishes.add(wish);
			}));
			regStudent.setWishes(wishes);

			imagePath = MvcUriComponentsBuilder
					.fromMethodName(PhotoController.class, "getFile", student.getPhoto()).build().toString();

			modelAndView.addObject("regStudent", regStudent);
			modelAndView.addObject("imagePath", imagePath);
			prepareTracksTable(modelAndView);

			return modelAndView;
		}

	}

	/**
	 * Prepare the student tracks table
	 *
	 * @param model to add required objects and send to the view.
	 */
	private void prepareTracksTable(ModelAndView model) {
		//create the list object for the activities to be sent to the view.
		List<Activity> activities = new ArrayList<>();
		List<House>    houses     = new ArrayList<>();

		//get the list of activities and houses from the DB
		Iterable<Activity> itrActivities = activityService.findAllOrderByName();
		Iterable<House>    itrHouses     = houseService.findAllOrderById();
		//fill activities and houses lists with data from DB
		itrActivities.forEach((activity -> activities.add(activity)));
		itrHouses.forEach((house) -> houses.add(house));


		//send the list of activities to the view.
		model.addObject("activities", itrActivities);
		//send the houses list to the view.
		model.addObject("houses", itrHouses);
		//send the status values to the view
		model.addObject("trackStatuses", StudentStatus.values());
		//send the countries to the view.
		model.addObject("countries", countryService.findAllByOrderByArabicNameAsc());
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

	/**
	 * Get the register view to add new student
	 *
	 * @return the register student view
	 */
	@GetMapping("/admin/students/student")
	public ModelAndView getRegisterStudentView() {
		//set the model view.
		ModelAndView model = new ModelAndView("admin/register-student");
		//prepareModel(model, null);
		//student.setStudentTracks(tracks);
		RegisteringStudent regStudent = new RegisteringStudent();
		List<Wish>         wishes     = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			//tracks.add(new StudentTrack());
			wishes.add(new Wish());
		}
		regStudent.setWishes(wishes);
		//send object of the registering student to the view to be filled
		model.addObject("regStudent", regStudent);
		prepareTracksTable(model);

		return model;
	}

	/**
	 * Update student data and redirect to his profile with updated data
	 *
	 * @param regStudent    the form object of update student data
	 * @param image         the profile photo
	 * @param id            the id of student to update his data
	 * @param bindingResult to bind errors with the object regstudent
	 * @return redirected URL of student profile after update
	 */
	@PostMapping("/admin/students/student/{id}")
	public String updateStudent(@Valid RegisteringStudent regStudent, @RequestParam("image") MultipartFile
			image, @PathVariable Integer id, BindingResult bindingResult) {
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
		regStudent.getWishes().stream()
				.filter(wish -> wish.isSelected())
				.forEach(wish -> {
					student.getStudentTracks().add(wish.getTrack());
				});
		student.setHouse(regStudent.getHouse());

		if (null == image) {
			System.out.println("file is null");
			student.setPhoto(student.getPhoto());
		} else {
			try {
				storageService.store(image, LocationTag.STUDENTS_STORE_LOC);
				storageService.deletePhotoByName(student.getPhoto(), LocationTag.STUDENTS_STORE_LOC);
				student.setPhoto(image.getOriginalFilename());
			} catch (Exception e) {
				log.error("Fails to Store the image!");
				log.error(e.toString());
			}
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
	public String registerStudent(@Valid RegisteringStudent regStudent, @RequestParam("image") MultipartFile image,
	                              BindingResult bindingResult) {

		List<StudentTrack> tracks = new ArrayList<>();

		if (bindingResult.hasErrors()) {
			return "admin/register-student";
		} else {
			Student returnedStudent = null;
			if (regStudent.getWishes() != null) {
				System.out.println("Not Null Wish...");
				regStudent.getWishes().stream()
						.filter((wish) -> wish.isSelected())
						.forEach((wish) -> {
							System.out.println("Wish: " + wish.toString());

							if (wish.getActivityId() == 7) {
								System.out.println("Choose housing...");
								regStudent.setHouse(wish.getHouse());
							} else {
								StudentTrack track = new StudentTrack();
								//track.setStudent(regStudent);
								track.setCourse(wish.getTrack().getCourse());
								track.setCertificate(wish.getTrack().getCertificate());
								track.setRegisterDate(wish.getTrack().getRegisterDate());
								track.setStartDate(wish.getTrack().getStartDate());
								track.setStatus(wish.getTrack().getStatus());
								track.setEvaluation(wish.getTrack().getEvaluation());
								track.setComments(wish.getTrack().getComments());

								tracks.add(track);
								regStudent.setStudentTracks(tracks);
							}

						});

			}
			System.out.println("Image: " + image.getOriginalFilename());
			System.out.println("Student: " + regStudent);
			if (image.getOriginalFilename().isEmpty()) {
				System.out.println("Image: avatar5.png");
				regStudent.setPhoto("avatar5.png");
			} else {
				try {
					storageService.store(image, LocationTag.STUDENTS_STORE_LOC);
					regStudent.setPhoto(image.getOriginalFilename());
				} catch (Exception e) {
					log.error("Fails to Store the image!");
					log.error(e.toString());
				}
			}

			returnedStudent = studentService.registerStudent(regStudent);
			return "redirect:/admin/students/student/" + returnedStudent.getId();
		}

	}


}
