package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.StudentTrack;
import com.omarionapps.halaka.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by Omar on 02-Jun-17.
 */
@Service
public class StudentTrackConverter implements Formatter<StudentTrack> {
	@Autowired
	StudentTrackService trackService;

	@Override
	public StudentTrack parse(String s, Locale locale) {
		if (null != s) {
			int id = Utils.convertToID(s);
			return trackService.findById(id).get();
		}

		return new StudentTrack();
	}

	@Override
	public String print(StudentTrack track, Locale locale) {
		return (track != null) ? String.valueOf(track.getId()) : "";
	}
}
