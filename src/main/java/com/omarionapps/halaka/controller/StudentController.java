package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.*;
import com.omarionapps.halaka.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omar on 09-Apr-17.
 */
@Controller
public class StudentController {
    Logger logger = LoggerFactory.getLogger(StudentController.class);
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

    @GetMapping("/admin/students")
    public ModelAndView getStudentsList() {
        ModelAndView model = new ModelAndView("admin/student-list");
        Iterable<Student> students = studentService.getAll();
        model.addObject("students", students);

        return model;
    }

    @GetMapping("/admin/students/student")
    public ModelAndView processStudentRequest(@RequestParam(value = "id", required = false) Integer id) {
        return (null == id) ? addStudent() : getStudent(id);
    }

    /**
     * Create model for add course page
     *
     * @return model of page with its attributes
     */
    private ModelAndView addStudent() {
	    //set the model view.
        ModelAndView model = new ModelAndView("admin/register-student");
	    //create the student and the registering student objects
	    RegisteringStudent registeringStudent = new RegisteringStudent();
	    Student            student            = new Student();
	    //create the list of wishes
	    List<Wish> wishes = new ArrayList<>();

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

        for (int i = 0; i < 7; i++) {
	        //tracks.add(new StudentTrack());
	        wishes.add(new Wish());
        }

	    //student.setStudentTracks(tracks);
	    registeringStudent.setStudent(student);
	    registeringStudent.setWishes(wishes);
        //System.out.println("ActivityCourses: " + activityService.getActivityCourses());
	    //model.addObject("student", student);
	    //send object of the registering student to the view to be filled
	    model.addObject("reg_student", registeringStudent);
	    //send the list of activities to the view.
	    model.addObject("activities", activities);
	    //send the houses list to the view.
	    model.addObject("houses", houses);
        /*
        for (StudentStatus sts : StudentStatus.values()) {
            System.out.println(sts.getTrackStatus());
        }
        */
	    //send the status values to the view
        model.addObject("trackStatuses", StudentStatus.values());
        //model.addObject("activityCourses", activityService.getActivityCourses());
	    //send the countries to the view.
        model.addObject("countries", countryService.findAllByOrderByArabicNameAsc());

        return model;
    }

    /**
     * Create the page of details of a specific course
     *
     * @param id the id of required course to get its details
     * @return model of the page with its details of the course.
     */
    private ModelAndView getStudent(int id) {
        ModelAndView modelAndView = new ModelAndView("admin/student-profile");
        Student student = studentService.getById(id);

        modelAndView.addObject("student", student);
	    System.out.println("Student Profile: " + student);
        modelAndView.addObject("countries", countryService.findAllByOrderByEnglishNameAsc());
        return modelAndView;
    }


    /**
     * Process the get request of delete operation
     *
     * @param id            the course id to be deleted
     * @param redirectAttrs to send message of success or failure of the operation
     * @return redirect to the same page of courses list with new info and delete operation result message
     */
    @GetMapping("/admin/students/student/archive")
    public String deleteStudent(@RequestParam(value = "id") int id, RedirectAttributes redirectAttrs) {
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
     * @param reg_student the student to be updated or created
     * @return redirect to the saved student profile page
     */
    @PostMapping("/admin/students/student")
    public String registerStudent(@Valid RegisteringStudent reg_student, BindingResult bindingResult, Model model) {
	    List<StudentTrack> tracks = new ArrayList<>();

        if (bindingResult.hasErrors()) {
	        return (reg_student.getId() == 0) ? "admin/register-student" : "redirect:/admin/students/student?id=" + reg_student.getStudent().getId();

        } else {
            Student returnedStudent = null;
	        if (reg_student.getWishes() == null) {
		        System.out.println("Null wishes");
	        } else {
		        System.out.println("Not Null Wish...");
		        reg_student.getWishes().stream().filter((wish) -> wish.isSelected())
				        .forEach((wish) -> {
					        System.out.println("Wish: " + wish.toString());

					        if (wish.getActivityId() == 7) {
						        System.out.println("Choose housing...");
						        reg_student.getStudent().setHouse(wish.getHouse());
					        } else {
						        StudentTrack track = new StudentTrack();
						        track.setStudent(reg_student.getStudent());
						        track.setRegisterDate(wish.getRegisterDate());
						        track.setCertificate(wish.getCertificate());
						        track.setCourse(wish.getCourse());
						        track.setComments(wish.getComments());
						        track.setEvaluation(wish.getEvaluation());
						        track.setStartDate(wish.getStartDate());
						        track.setStatus(wish.getStatus());

						        tracks.add(track);
						        reg_student.getStudent().setStudentTracks(tracks);
					        }

				        });
		        //System.out.println("Student: " + student);
		        returnedStudent = studentService.save(reg_student.getStudent());

	        }
            return "redirect:/admin/students/student?id=" + returnedStudent.getId();
        }

    }
}
