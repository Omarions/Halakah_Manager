package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Task;
import com.omarionapps.halaka.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by Omar on 05-Aug-17.
 */
@Service
public class TaskService {
    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Iterable<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(int id) {
        return taskRepository.findOne(id);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }
}
