package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Event;
import com.omarionapps.halaka.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by Omar on 02-Jun-17.
 */
@Service
public class EventConverter implements Formatter<Event> {
	@Autowired
	EventService eventService;

	@Override
	public Event parse(String s, Locale locale) {
		if (null != s) {
			int id = Utils.convertToID(s);
			return eventService.findById(id).get();
		}

		return new Event();
	}

	@Override
	public String print(Event event, Locale locale) {
		return (event != null) ? String.valueOf(event.getId()) : "";
	}
}
