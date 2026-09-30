package com.accenture.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Task Manager.
 *
 * @author Javier Roldán
 * @version 1.0
 */
@SpringBootApplication
public class TaskmanagerApplication {

	/**
	 * Punto de entrada de la aplicación.
	 *
	 * @param args argumentos recibidos desde la línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(TaskmanagerApplication.class, args);
	}

}
