package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Event;
import com.omarionapps.halaka.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
	private EventRepository eventRepository;

	@Autowired
	public EventService(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

	public List<Event> getAllOrderByEventDate() {
		return eventRepository.findAllByOrderByEventDate();
	}

	public Optional<Event> findById(int eventId) {
		return eventRepository.findById(eventId);
	}

	public long getCount() {
		return eventRepository.findAllByOrderByEventDate().size();
	}

	public Event save(Event event) {
		return eventRepository.save(event);
	}
}
