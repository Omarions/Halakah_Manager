package com.omarionapps.halaka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Omar on 05-Aug-17.
 */
@Controller
public class CertificateController {

	@GetMapping("/admin/certificates")
	public ModelAndView getCertificates() {
		return new ModelAndView("admin/certificate-list");
	}

	@GetMapping("/admin/certificates/certificate")
	public ModelAndView addCertificates() {
		return new ModelAndView("admin/register-certificate");
	}

}
