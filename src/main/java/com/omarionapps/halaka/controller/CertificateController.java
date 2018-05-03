package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Certificate;
import com.omarionapps.halaka.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

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

	/**
	 * Send the certificate to delete
	 *
	 * @param id            the certificate ID to be archived
	 * @param redirectAttrs the message to be send with the link to be directed to after archiving success.
	 * @return the link to be directed to after success.
	 */
	@GetMapping("/admin/certificates/certificate/delete")
	public String deleteCertificate(@RequestParam(value = "id") int id, RedirectAttributes redirectAttrs) {
		Optional<Certificate> cert = certificateService.findById(id);
		if (null != cert) {
			certificateService.delete(id);
			redirectAttrs.addFlashAttribute("message", "Certificate with ID( " + id + " ) has been deleted successfully");
		} else {
			redirectAttrs.addFlashAttribute("message", "An error happens while deleting the certificate with ID( " +
					id + " )");
		}
		return "redirect:/admin/certificates";
	}
}
