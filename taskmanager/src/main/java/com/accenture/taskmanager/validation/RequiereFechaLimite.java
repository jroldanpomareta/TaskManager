package com.accenture.taskmanager.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// Crea la anotación personalizada de validación RequiereFechaLimite a nivel de clase, utilizando @Constraint y vinculándola a RequiereFechaLimiteValidator. Incluye los elementos obligatorios de una anotación de Bean Validation y mensajes en español. — generado por IA
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RequiereFechaLimiteValidator.class)
public @interface RequiereFechaLimite {

    String message() default "La fecha límite es obligatoria cuando la prioridad es ALTA";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}