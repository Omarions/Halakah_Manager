package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Certificate;
import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.StudentStatus;
import com.omarionapps.halaka.model.StudentTrack;
import com.omarionapps.halaka.service.CertificateService;
import com.omarionapps.halaka.service.EventService;
import com.omarionapps.halaka.service.StorageService;
import com.omarionapps.halaka.service.StudentTrackService;
import com.omarionapps.halaka.utils.LocationTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Omar on 05-Aug-17.
 */
@Controller
public class CertificateController {

	@Autowired
	CertificateService  certificateService;
	@Autowired
	StudentTrackService studentTrackService;
	@Autowired
	EventService        eventService;
	@Autowired
	StorageService      storageService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/admin/certificates")
	public ModelAndView getCertificates() {
		List<Certificate> certs        = new ArrayList<>();
		ModelAndView      modelAndView = new ModelAndView("admin/certificate-list");
		certificateService.findAll().spliterator().forEachRemaining(certificate -> certs.add(certificate));
		certs.stream()
				.forEach(certificate -> {
					String certPhotoUrl = MvcUriComponentsBuilder
							.fromMethodName(PhotoController.class, "getFile", certificate.getPhoto(), LocationTag.CERT_STORE_LOC).build().toString();
					certificate.setImageUrl(certPhotoUrl);
				});

		modelAndView.addObject("certificates", certs);

		return modelAndView;
	}

	/**
	 * Send the certificate to delete
	 *
	 * @param certId        the certificate ID to be archived
	 * @param redirectAttrs the message to be send with the link to be directed to after archiving success.
	 * @return the link to be directed to after success.
	 */
	@GetMapping("/admin/certificates/certificate/{certId}")
	public ModelAndView getCertificateProfileView(@PathVariable(value = "certId") int certId, RedirectAttributes
			redirectAttrs) {
		ModelAndView          modelAndView = new ModelAndView("admin/certificate-profile");
		Optional<Certificate> cert         = certificateService.findById(certId);
		Certificate           certificate  = cert.get();
		Student               student      = certificate.getStudentTrack().getStudent();
		String photoUrl = MvcUriComponentsBuilder.
				fromMethodName(PhotoController.class,
						"getFile",
						student.getPhoto(),
						LocationTag.STUDENTS_STORE_LOC)
				.build()
				.toString();

		student.setPhotoUrl(photoUrl);

		if (null != cert) {
			redirectAttrs.addFlashAttribute("msgCertDeleteSuccess", "Certificate with ID( " + certId + " ) has been deleted successfully");
		} else {
			redirectAttrs.addFlashAttribute("msgCertDeleteError", "An error happens while deleting the certificate with ID( " +
					certId + " )");
		}

		modelAndView.addObject("certificate", certificate);
		modelAndView.addObject("regStudent", cert.get().getStudentTrack().getStudent());
		modelAndView.addObject("events", eventService.getAllOrderByEventDate());

		return modelAndView;
	}


	/**
	 * Send the certificate to delete
	 *
	 * @param certId        the certificate ID to be archived
	 * @param redirectAttrs the message to be send with the link to be directed to after archiving success.
	 * @return the link to be directed to after success.
	 */
	@GetMapping("/admin/certificates/certificate/{certId}/delete")
	public String deleteCertificate(@PathVariable(value = "certId") int certId, RedirectAttributes redirectAttrs) {
		Optional<Certificate> cert         = certificateService.findById(certId);
		int                   studentId    = cert.get().getStudentTrack().getStudent().getId();
		StudentTrack          studentTrack = cert.get().getStudentTrack();


		if (null != cert) {
			certificateService.delete(certId);
			studentTrack.setStatus(StudentStatus.STUDYING.toString());
			studentTrackService.save(studentTrack);
			redirectAttrs.addFlashAttribute("msgCertDeleteSuccess", "Certificate with ID( " + certId + " ) has been deleted successfully");
		} else {
			redirectAttrs.addFlashAttribute("msgCertDeleteError", "An error happens while deleting the certificate with ID( " +
					certId + " )");
		}

		return "redirect:/admin/students/student/" + studentId;
	}

	@PostMapping("admin/certificates/certificate")
	public String addCertificate(Certificate certificate, @RequestParam("imageFile") MultipartFile image) {
		Certificate m_certificate = new Certificate();

		if (image.getOriginalFilename().isEmpty()) {
			m_certificate.setPhoto("certificate_default.jpg");
		} else {
			try {
				storageService.store(image, LocationTag.CERT_STORE_LOC);
				m_certificate.setPhoto(image.getOriginalFilename());
			} catch (Exception e) {
				log.error("Fails to Store the image!");
				log.error(e.toString());
			}
		}
		m_certificate.setName(certificate.getName());
		m_certificate.setEvent(certificate.getEvent());
		m_certificate.setStudentTrack(certificate.getStudentTrack());
		m_certificate.setComments(certificate.getComments());

		Certificate  newCert      = certificateService.save(m_certificate);
		StudentTrack updatedTrack = newCert.getStudentTrack();
		updatedTrack.setStatus(StudentStatus.CERTIFIED.toString());
		studentTrackService.save(updatedTrack);

		return "redirect:/admin/students/student/" + newCert.getStudentTrack().getStudent().getId();
	}

	@PostMapping("admin/certificates/certificate/{certId}")
	public String updateCertificate(Certificate certificate, @PathVariable(value = "certId") Integer certId, @RequestParam("imageFile") MultipartFile image) {
		Certificate m_certificate = certificateService.findById(certId).get();

		if (image.getOriginalFilename().isEmpty()) {
			m_certificate.setPhoto("certificate_default.jpg");
		} else {
			try {
				storageService.store(image, LocationTag.CERT_STORE_LOC);
				storageService.deletePhotoByName(m_certificate.getPhoto(), LocationTag.CERT_STORE_LOC);
				m_certificate.setPhoto(image.getOriginalFilename());
			} catch (Exception e) {
				log.error("Fails to Store the image!");
				log.error(e.toString());
			}
		}
		m_certificate.setName(certificate.getName());
		m_certificate.setEvent(certificate.getEvent());
		m_certificate.setStudentTrack(certificate.getStudentTrack());
		m_certificate.setComments(certificate.getComments());

		Certificate updatedCert = certificateService.save(m_certificate);

		return "redirect:/admin/students/student/" + updatedCert.getStudentTrack().getStudent().getId();
	}


}
