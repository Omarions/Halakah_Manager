package com.omarionapps.halaka.repository;

import com.omarionapps.halaka.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Integer> {
	List<Event> findAllByOrderByEventDate();

	List<Event> findAllByEventDateBetween(Date start, Date end);
}
