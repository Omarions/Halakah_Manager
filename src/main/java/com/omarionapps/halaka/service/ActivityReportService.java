package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Activity;
import com.omarionapps.halaka.model.ActivityReport;
import com.omarionapps.halaka.utils.StudentGender;
import com.omarionapps.halaka.utils.StudentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ActivityReportService {
	@Autowired
	ActivityService activityService;

	public List<ActivityReport> getAll(){
		List<ActivityReport> reports = new ArrayList<>();

		activityService.findAllByOrderByName().forEach(activity -> {
			ActivityReport activityReport = getByActivity(activity);
			reports.add(activityReport);
		});
		
		return reports;
	}

	public ActivityReport getByActivity(Activity activity){
		ActivityReport activityReport = new ActivityReport();
		long totalStudying = activityService.getTotalStudentsByActivityByStatus(activity, StudentStatus.STUDYING);
		long totalTempStopped = activityService.getTotalStudentsByActivityByStatus(activity, StudentStatus.TEMP_STOP);

		long totalActiveStudents = totalStudying + totalTempStopped;
		long totalWaitingStudents = activityService.getTotalStudentsByActivityByStatus(activity, StudentStatus.WAITING);
		long totalCertifiedStudents = activityService.getTotalStudentsByActivityByStatus(activity, StudentStatus.CERTIFIED);
		long totalCertificates = activityService.findCertificatesByActivity(activity).size();
		long totalFemales = activityService.getTotalStudentsByActivityAndGender(activity, StudentGender.FEMALE);
		long totalMales = activityService.getTotalStudentsByActivityAndGender(activity, StudentGender.MALE);
		long totalStudentsTeachers = activityService.getTotalStudentsTeachersByActivity(activity);
		long totalActiveStudentForST = activityService.getTotalActiveStudentsForSTByActivity(activity);
		long totalStudentsNationalities = activityService.getTotalNationalitiesByActivity(activity);
		long totalCSNationalities = activityService.getTotalNationalitiesByActivityAndStatus(activity, StudentStatus.CERTIFIED);
		
		activityReport.setActivity(activity);
		activityReport.setTotalActiveStudents(totalActiveStudents);
		activityReport.setTotalWaitingStudents(totalWaitingStudents);
		activityReport.setTotalCertifiedStudents(totalCertifiedStudents);
		activityReport.setTotalCertificates(totalCertificates);
		activityReport.setTotalCSNationalities(totalCSNationalities);
		activityReport.setTotalFemales(totalFemales);
		activityReport.setTotalMales(totalMales);
		activityReport.setTotalStudentsForST(totalActiveStudentForST);
		activityReport.setTotalStudentsNationalities(totalStudentsNationalities);
		activityReport.setTotalStudentTeacher(totalStudentsTeachers);

		return activityReport;
	}
}
