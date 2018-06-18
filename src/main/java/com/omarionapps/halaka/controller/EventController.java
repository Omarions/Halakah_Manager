package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Event;
import com.omarionapps.halaka.model.EventStatus;
import com.omarionapps.halaka.service.EventService;
import com.omarionapps.halaka.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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

	@PostMapping("/admin/events/event")
	public String addNewEvent(@Valid Event event) {

		Event saveEvent = eventService.save(event);

		return "redirect:/admin/events/event/" + saveEvent.getId();
	}

}