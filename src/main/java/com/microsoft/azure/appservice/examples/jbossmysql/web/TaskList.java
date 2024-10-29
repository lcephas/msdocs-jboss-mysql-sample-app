package com.microsoft.azure.appservice.examples.jbossmysql.web;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.microsoft.azure.appservice.examples.jbossmysql.model.TaskRepository;
import com.microsoft.azure.appservice.examples.jbossmysql.model.entity.Task;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.PersistenceException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Named
@RequestScoped
public class TaskList implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
	private TaskRepository taskRepository;

	@NotNull
	@NotEmpty
	protected String name;

	protected List<Task> taskList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Task> getTaskList() {
		return taskList;
	}

	@PostConstruct
	private void init() {
		try {
			this.getAllTasks();
		} catch (PersistenceException ex) {
			logger.log(Level.SEVERE, "Error calling getAllTasks(): {0}.", ex);
		}
	}

	private void getAllTasks() {
		logger.log(Level.INFO, "In getAllTasks(). Retrieving tasks now...");
		this.taskList = this.taskRepository.getAllTasks();
	}

	public void addTask() {
		Task task = new Task(this.name);

		try {
			this.taskRepository.persistTask(task);
		} catch (PersistenceException ex) {
			logger.log(Level.SEVERE, "Error creating task {0}: {1}.", new Object[] { task, ex });
		}
		this.name = null;
		this.getAllTasks();
	}

	public void deleteTask(String taskId) {
        try {
			this.taskRepository.removeTaskById(Long.valueOf(taskId));
		} catch (IllegalArgumentException ex) {
			logger.log(Level.SEVERE, "Error calling deleteTask() for taskId {0}: {1}.",
					new Object[] { taskId, ex });
		}
		this.getAllTasks();
	}
}
