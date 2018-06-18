package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.service.StorageService;
import com.omarionapps.halaka.utils.LocationTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PhotoController {
	List<String> files = new ArrayList<String>();
	@Autowired
	private StorageService storageService;

	@GetMapping("/admin/files")
	public String getListFiles(Model model) {
		model.addAttribute("files",
				files.stream()
						.map(fileName -> MvcUriComponentsBuilder
								.fromMethodName(PhotoController.class, "getFile", fileName).build().toString())
						.collect(Collectors.toList()));

		return "listFiles";
	}

	@GetMapping("/admin/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename, LocationTag locTag) {
		Resource file = null;

		if (filename == null) {
			file = storageService.loadFile(filename, LocationTag.ACTIVITY_STORE_LOC);
		}

		switch (locTag) {
			case ACTIVITY_STORE_LOC:
				file = storageService.loadFile(filename, LocationTag.ACTIVITY_STORE_LOC);
				break;
			case STUDENTS_STORE_LOC:
				file = storageService.loadFile(filename, LocationTag.STUDENTS_STORE_LOC);
				break;
			case TEACHERS_STORE_LOC:
				file = storageService.loadFile(filename, LocationTag.TEACHERS_STORE_LOC);
				break;
			case CERT_STORE_LOC:
				file = storageService.loadFile(filename, LocationTag.CERT_STORE_LOC);
				break;
			case EVENTS_STORE_LOC:
				file = storageService.loadFile(filename, LocationTag.EVENTS_STORE_LOC);
				break;

		}


		return ResponseEntity.ok()
				.body(file);
	}
}
