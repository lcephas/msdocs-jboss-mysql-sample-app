package com.microsoft.azure.appservice.examples.jbossmysql.model;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.microsoft.azure.appservice.examples.jbossmysql.model.entity.Task;

@Stateless
public class TaskRepository {

	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

	@PersistenceContext
	private EntityManager entityManager;

	public List<Task> getAllTasks() {
		logger.log(Level.INFO, "Finding all tasks. ");

		return this.entityManager.createNamedQuery("findAllTasks", Task.class).getResultList();
	}

	public Task persistTask(Task task) {
		logger.log(Level.INFO, "Persisting the new task {0}.", task);
		this.entityManager.persist(task);
		return task;
	}

	public void removeTaskById(Long taskId) {
		logger.log(Level.INFO, "Removing a task {0}.", taskId);
		Task task = entityManager.find(Task.class, taskId);
		this.entityManager.remove(task);
	}

	public Task findTaskById(Long taskId) {
		logger.log(Level.INFO, "Finding the task with id {0}.", taskId);
		return this.entityManager.find(Task.class, taskId);
	}
}
