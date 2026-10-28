package com.accenture.taskmanager;

/**
 * This class represents a custom exception that is thrown when a task is not found in the task manager application.
 * It extends the RuntimeException class, allowing it to be thrown without being explicitly declared in method signatures.
 * 
 * Usage:
 * This exception can be used in scenarios where a requested task cannot be located, providing a clear indication of the error.
 * 
 * Example:
 * if (task == null) {
 *     throw new TaskNotFoundException("Task with ID " + taskId + " not found.");
 * }
 * 
 * @author Javier Roldán
 * @version 1.0
 */
public class TaskNotFoundException {

}
