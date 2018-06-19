package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Event;
import com.omarionapps.halaka.model.EventStatus;
import com.omarionapps.halaka.service.EventService;
import com.omarionapps.halaka.service.StorageService;
import com.omarionapps.halaka.utils.LocationTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;

/**
 * Created by Omar on 14-May-17.
 */
@Controller
public class EventController {
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());

	private StorageService storageService;
	private EventService   eventService;

	@Autowired
	public EventController(StorageService storageService,
	                       EventService eventService) {
		this.storageService = storageService;
		this.eventService = eventService;
	}

	@GetMapping("/admin/events")
	public ModelAndView getEventListView() {
		ModelAndView model = new ModelAndView("admin/event-list");
		model.addObject("events", eventService.getAllOrderByEventDate());
		model.addObject("eventStatuses", EventStatus.values());
		return model;
	}

	/**
	 * Process the get event request, and show the profile page
	 *
	 * @param eventId the request parameter that sent with the request
	 * @return the page of appropriate request based on request param that sent
	 */
	@GetMapping("/admin/events/event/{eventId}")
	public ModelAndView getProfileView(@PathVariable(value = "eventId") Integer eventId) {
		ModelAndView modelAndView = new ModelAndView("admin/event-profile");
		Event        event        = eventService.findById(eventId).get();

		event.getCertificates().stream().forEach(certificate -> {
			String photoUrl = MvcUriComponentsBuilder.fromMethodName(PhotoController.class, "getFile", certificate
					.getPhoto(), LocationTag.CERT_STORE_LOC).build().toString();
			certificate.setImageUrl(photoUrl);
		});


		modelAndView.addObject("event", event);
		modelAndView.addObject("eventStatuses", EventStatus.values());

		return modelAndView;
	}

	/**
	 * Process the get event request, to shows the page of registering event
	 *
	 * @return the page of registering event
	 */
	@GetMapping("/admin/events/event")
	public ModelAndView getRegisterEventView() {
		ModelAndView model = new ModelAndView("admin/register-event");

		model.addObject("event", new Event());
		model.addObject("eventStatuses", EventStatus.values());
		return model;
	}
	@PostMapping("/admin/events/event")
	public String addNewEvent(@Valid Event event) {

		Event saveEvent = eventService.save(event);

		return "redirect:/admin/events/event/" + saveEvent.getId();
	}

}