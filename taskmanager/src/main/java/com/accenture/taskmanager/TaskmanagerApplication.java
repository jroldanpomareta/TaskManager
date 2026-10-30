package com.accenture.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.accenture.taskmanager.enums.Role;
import com.accenture.taskmanager.model.AppUser;
import com.accenture.taskmanager.repository.AppUserRepository;

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

	//Implementa un bean CommandLineRunner que cree un usuario administrador al iniciar la aplicación si no existe. Inyecta AppUserRepository y PasswordEncoder. Crea el usuario con username "admin", contraseña "Admin123!" codificada con passwordEncoder.encode() y rol Role.ADMIN. Guarda el usuario mediante el repositorio. Añade los imports necesarios y Javadoc al método. No modifiques el método main ni dupliques métodos existentes. — generado por IA
	/**
	 * Crea un usuario administrador al iniciar la aplicación si no existe.
	 *
	 * @param appUserRepository repositorio de usuarios
	 * @param passwordEncoder   codificador de contraseñas
	 * @return proceso de inicialización del administrador
	 */
	@Bean
	CommandLineRunner createAdmin(
			AppUserRepository appUserRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			String username = "admin";

			if (!appUserRepository.existsByUsername(username)) {
				AppUser admin = new AppUser(
						username,
						passwordEncoder.encode("Admin123!"),
						Role.ADMIN);

				appUserRepository.save(admin);
			}
		};
	}

}
