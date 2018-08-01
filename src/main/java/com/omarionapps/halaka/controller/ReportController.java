package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.service.ActivityReportService;
import com.omarionapps.halaka.utils.LocationTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
public class ReportController {
	private ActivityReportService activityReportService;

	@Autowired
	public ReportController(ActivityReportService activityReportService) {
		this.activityReportService = activityReportService;
	}

	@GetMapping("/admin/report")
	public ModelAndView getReportView() {
		ModelAndView model = new ModelAndView("admin/general-report");
		prepareModel(model);
		return model;
	}

	@GetMapping("/admin/print_general_report")
	public ModelAndView getGeneralReportPrintView(){
		ModelAndView model = new ModelAndView("admin/general-report-print");

		prepareModel(model);

		return model;
	}

	private void prepareModel(ModelAndView model) {
		activityReportService.getAll()
		                     .stream()
		                     .map(activityReport -> activityReport.getActivity())
		                     .forEach(activity -> {
			                     String logoUrl = MvcUriComponentsBuilder
					                                      .fromMethodName(PhotoController.class,
							                                      "getFile",
							                                      activity.getLogo(), LocationTag.ACTIVITY_STORE_LOC)
					                                      .build()
					                                      .toString();

			                     activity.setLogoUrl(logoUrl);
		                     });

		model.addObject("reports", activityReportService.getAll());
	}
}
