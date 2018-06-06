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

	private StudentService      studentService;
	private ActivityService     activityService;
	private CourseService       courseService;
	private CountryService      countryService;
	private HouseService        houseService;
	private StorageService      storageService;
	private StudentTrackService studentTracksService;

	@Autowired
	public StudentController(StudentService studentService, ActivityService activityService, CourseService courseService,
	                         HouseService houseService, CountryService countryService, StorageService storageService, StudentTrackService studentTrackService) {
		this.studentService = studentService;
		this.activityService = activityService;
		this.courseService = courseService;
		this.houseService = houseService;
		this.countryService = countryService;
		this.storageService = storageService;
		this.studentTracksService = studentTrackService;
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

		Optional<Student> optStudent = studentService.findById(id);
		Student           student    = optStudent.get();
		if (null == student) {
			modelAndView = new ModelAndView("admin/students-list");
			modelAndView.addObject("messageError", "There is no student with ID (" + id + ")...");
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

			imagePath = MvcUriComponentsBuilder
					.fromMethodName(PhotoController.class, "getFile", student.getPhoto(), LocationTag.STUDENTS_STORE_LOC).build()
					.toString();


			modelAndView.addObject("imagePath", imagePath);
			prepareTracksTable(modelAndView, regStudent);

			return modelAndView;
		}

	}

	/**
	 * Prepare the student tracks table
	 *
	 * @param regStudent the view object to add wishes that sent to the view
	 * @param model      to add required objects and send to the view.
	 */
	private void prepareTracksTable(ModelAndView model, RegisteringStudent regStudent) {
		//create the list object for the activities to be sent to the view.
		List<Activity> activities = new ArrayList<>();
		List<House>    houses     = new ArrayList<>();

		List<Wish> wishes = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			//tracks.add(new StudentTrack());
			wishes.add(new Wish());
		}
		regStudent.setWishes(wishes);

		//get the list of activities and houses from the DB
		Iterable<Activity> itrActivities = activityService.findAllOrderByName();
		Iterable<House>    itrHouses     = houseService.findAllOrderById();
		//fill activities and houses lists with data from DB
		itrActivities.forEach((activity -> activities.add(activity)));
		itrHouses.forEach((house) -> houses.add(house));


		model.addObject("regStudent", regStudent);
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

		prepareTracksTable(model, regStudent);

		return model;
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
		Optional<Student> optStudent = studentService.findById(id);
		Student           student    = optStudent.get();
		student.setArchived(true);
		student.setArchivedDate(Date.valueOf(LocalDate.now()));
		Student archivedStudent = studentService.save(student);
		if (null != archivedStudent) {
			redirectAttrs.addFlashAttribute("messageSuccess", "Student with ID( " + id + " ) was archived successfully");
		} else {
			redirectAttrs.addFlashAttribute("messageError", "An error happens while archiving the student with ID( " +
					id + " )");
		}

		return "redirect:/admin/students";
	}

	/**
	 * Update student data and redirect to his profile with updated data
	 *
	 * @param regStudent    the form object of update student data
	 * @param image         the profile photo
	 * @param studentId     the id of student to update his data
	 * @param bindingResult to bind errors with the object regstudent
	 * @return redirected URL of student profile after update
	 */
	@PostMapping("/admin/students/student/{studentId}")
	public String updateStudent(@Valid RegisteringStudent regStudent, @RequestParam("image") MultipartFile
			image, @PathVariable Integer studentId, BindingResult bindingResult) {
		List<StudentTrack> tracks  = new ArrayList<>();
		Student            student = studentService.findById(studentId).get();
		student.setName(regStudent.getName());
		student.setGender(regStudent.getGender());
		student.setIdentityId(regStudent.getIdentityId());
		student.setBirthDate(regStudent.getBirthDate());
		student.setBirthLocation(regStudent.getBirthLocation());
		student.setCountry(regStudent.getCountry());
		student.setHomeAddress(regStudent.getHomeAddress());
		student.setEgyptAddress(regStudent.getEgyptAddress());
		student.setTel(regStudent.getTel());
		student.setEmail(regStudent.getEmail());
		student.setFacebook(regStudent.getFacebook());
		student.setEducation(regStudent.getEducation());
		student.setJob(regStudent.getJob());
		student.setComments(regStudent.getComments());
		student.setArchived(false);
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
		//	System.out.println("New Student after update:" + student);
		Student returnedStudent = studentService.updateStudent(student);
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

							if (wish.getActivityId() == 7) {
								System.out.println("Choose housing...");
								regStudent.setHouse(wish.getHouse());
							} else {
								StudentTrack track = new StudentTrack();
								//track.setStudent(regStudent);
								track.setCourse(wish.getTrack().getCourse());
								System.out.println("Selected Course for track: " + track.getCourse().getId());
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


	/**
	 * add new tracks to the student and redirect to his profile with updated data
	 *
	 * @param studentId the id of student to update his data
	 * @return redirected URL of student profile after adding new tracks
	 */
	@PostMapping("/admin/students/student/{studentId}/tracks")
	public String addTracks(RegisteringStudent regStudent, @PathVariable Integer studentId) {

		Optional<Student>  optStudent = studentService.findById(studentId);
		List<StudentTrack> tracks     = assignTracks(regStudent.getWishes());
		if (optStudent.isPresent()) {
			Student student = optStudent.get();

			tracks.forEach((track -> track.setStudent(student)));

		}
		studentTracksService.saveAll(tracks);

		return "redirect:/admin/students/student/" + optStudent.get().getId();
	}

	private List<StudentTrack> assignTracks(List<Wish> wishes) {
		List<StudentTrack> tracks = new ArrayList<>();
		if (wishes != null) {
			System.out.println("Not Null Wish...");
			wishes.stream()
					.filter((wish) -> wish.isSelected())
					.forEach((wish) -> {

						if (wish.getActivityId() == 7) {
							System.out.println("Choose housing...");
							//regStudent.setHouse(wish.getHouse());
						} else {
							StudentTrack track = new StudentTrack();
							//track.setStudent(regStudent);
							track.setCourse(wish.getTrack().getCourse());
							System.out.println("Selected Course for track: " + track.getCourse().getId());
							track.setCertificate(wish.getTrack().getCertificate());
							track.setRegisterDate(wish.getTrack().getRegisterDate());
							track.setStartDate(wish.getTrack().getStartDate());
							track.setStatus(wish.getTrack().getStatus());
							track.setEvaluation(wish.getTrack().getEvaluation());
							track.setComments(wish.getTrack().getComments());

							tracks.add(track);
							//	regStudent.setStudentTracks(tracks);
						}

					});

		}
		return tracks;
	}
}
