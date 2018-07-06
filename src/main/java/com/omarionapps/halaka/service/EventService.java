package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Event;
import com.omarionapps.halaka.repository.EventRepository;
import com.omarionapps.halaka.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
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

	public int getTotalCertificates() {
		int totalCerts = getAllOrderByEventDate()
				                 .stream()
				                 .map(event -> event.getCertificates())
				                 .mapToInt(cert -> cert.size())
				                 .sum();

		return totalCerts;
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

	public double getCertIncrementRate(int year) {
		LocalDate start = LocalDate.of(year, 1, 1);
		LocalDate end   = LocalDate.of(year, 12, 31);


		double totalCerts = getAllOrderByEventDate()
				                    .stream()
				                    .map(event -> event.getCertificates())
				                    .mapToDouble(cert -> cert.size())
				                    .sum();
		double filterCertsCount = findAllByEventDateBetween(start, end)
				                          .stream()
				                          .map(event -> event.getCertificates())
				                          .mapToDouble(cert -> cert.size())
				                          .sum();
		double rate = (filterCertsCount / totalCerts) * 100;
		return rate;
	}

	public List<Event> findAllByEventDateBetween(LocalDate start, LocalDate end) {
		Date startDate = Utils.convertLocalDate(start);
		Date endDate   = Utils.convertLocalDate(end);
		return eventRepository.findAllByEventDateBetween(startDate, endDate);
	}

	
}
