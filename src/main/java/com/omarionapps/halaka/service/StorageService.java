package com.omarionapps.halaka.service;


import com.omarionapps.halaka.customeexceptions.StorageException;
import com.omarionapps.halaka.utils.LocationTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {
	private final String rootFolder             = "upload-dir";
	private final Path   rootLocationStudent    = Paths.get(rootFolder + File.separator + "students");
	private final Path   rootLocationTeacher    = Paths.get(rootFolder + File.separator + "teachers");
	private final Path   rootLocationCerts      = Paths.get(rootFolder + File.separator + "certificates");
	private final Path   rootLocationEvents     = Paths.get(rootFolder + File.separator + "events");
	private final Path   rootLocationActivities = Paths.get(rootFolder + File.separator + "activities");

	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	public void store(MultipartFile file, LocationTag locationTag) {
		switch (locationTag) {
			case STUDENTS_STORE_LOC:
				copyPhoto(file, rootLocationStudent);
				break;
			case TEACHERS_STORE_LOC:
				copyPhoto(file, rootLocationTeacher);
				break;
			case CERT_STORE_LOC:
				copyPhoto(file, rootLocationCerts);
				break;
			case EVENTS_STORE_LOC:
				copyPhoto(file, rootLocationEvents);
				break;
			case ACTIVITY_STORE_LOC:
				copyPhoto(file, rootLocationActivities);
				break;
		}
	}

	private void copyPhoto(MultipartFile photo, Path location) {
		String filename = StringUtils.cleanPath(photo.getOriginalFilename());
		try {
			if (photo.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory "
								+ filename);
			}
			Files.copy(photo.getInputStream(), location.resolve(filename),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

	public Resource loadFile(String filename, LocationTag locationTag) {
		Path path = null;
		switch (locationTag) {
			case STUDENTS_STORE_LOC:
				path = rootLocationStudent.resolve(filename);
				break;
			case TEACHERS_STORE_LOC:
				path = rootLocationTeacher.resolve(filename);
				break;
			case CERT_STORE_LOC:
				path = rootLocationCerts.resolve(filename);
				break;
			case EVENTS_STORE_LOC:
				path = rootLocationEvents.resolve(filename);
				break;
			case ACTIVITY_STORE_LOC:
				path = rootLocationActivities.resolve(filename);
				break;
		}
		//System.out.println("Load photo from path: " + path.toString());
		return loadPhoto(path);
	}

	private Resource loadPhoto(Path file) {
		try {
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageException("Failed to load file " + file.getFileName().toString());
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
		}
	}

	public void deleteAll(LocationTag locationTag) {
		switch (locationTag) {
			case STUDENTS_STORE_LOC:
				FileSystemUtils.deleteRecursively(rootLocationStudent.toFile());
				break;
			case TEACHERS_STORE_LOC:
				FileSystemUtils.deleteRecursively(rootLocationTeacher.toFile());
				break;
			case CERT_STORE_LOC:
				FileSystemUtils.deleteRecursively(rootLocationCerts.toFile());
				break;
			case EVENTS_STORE_LOC:
				FileSystemUtils.deleteRecursively(rootLocationEvents.toFile());
			case ACTIVITY_STORE_LOC:
				FileSystemUtils.deleteRecursively(rootLocationActivities.toFile());
				break;
		}
	}

	public void deletePhotoByName(String photoName, LocationTag locationTag) {
		Path path = null;
		switch (locationTag) {
			case STUDENTS_STORE_LOC:
				path = Paths.get(rootLocationStudent + File.separator + photoName);
				deletePhoto(path);
				break;
			case TEACHERS_STORE_LOC:
				path = Paths.get(rootLocationTeacher + File.separator + photoName);
				deletePhoto(path);
				break;
			case CERT_STORE_LOC:
				path = Paths.get(rootLocationCerts + File.separator + photoName);
				deletePhoto(path);
				break;
			case EVENTS_STORE_LOC:
				path = Paths.get(rootLocationEvents + File.separator + photoName);
				deletePhoto(path);
				break;
			case ACTIVITY_STORE_LOC:
				path = Paths.get(rootLocationActivities + File.separator + photoName);
				deletePhoto(path);
				break;
		}


	}

	private void deletePhoto(Path path) {
		if (path.toFile().exists()) {
			FileSystemUtils.deleteRecursively(path.toFile());
		}

	}

	public void init() {
		try {
			if (!Files.exists(rootLocationStudent))
				Files.createDirectory(rootLocationStudent);
			if (!Files.exists(rootLocationTeacher))
				Files.createDirectory(rootLocationTeacher);
			if (!Files.exists(rootLocationCerts))
				Files.createDirectory(rootLocationCerts);
			if (!Files.exists(rootLocationEvents))
				Files.createDirectory(rootLocationEvents);
			if (!Files.exists(rootLocationActivities))
				Files.createDirectory(rootLocationActivities);


		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage!");
		}
	}
}
