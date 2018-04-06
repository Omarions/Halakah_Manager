package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Omar on 05-Aug-17.
 */
@Controller
public class CertificateController {

	@Autowired
	CertificateService certificateService;

	@GetMapping("/admin/certificates")
	public ModelAndView getCertificates() {
		ModelAndView modelAndView = new ModelAndView("admin/certificate-list");
		modelAndView.addObject("certificates", certificateService.findAll());
		return modelAndView;
	}

	@GetMapping("/admin/certificates/certificate")
	public ModelAndView addCertificates() {
		return new ModelAndView("admin/register-certificate");
	}

}
