package com.accenture.taskmanager.validation;

import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.enums.TaskPriority;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Implementa ConstraintValidator para validar el TaskRequestDTO completo: si la prioridad es HIGH, la fecha límite debe estar informada; para las demás prioridades, esta regla no debe exigirla. Ten en cuenta que la fecha futura se valida por separado con @Future. — generado por IA
public class RequiereFechaLimiteValidator implements ConstraintValidator<RequiereFechaLimite, TaskRequestDTO> {

    @Override
    public boolean isValid(TaskRequestDTO value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value.getPriority() == TaskPriority.HIGH && value.getDueDate() == null) {
            return false;
        }

        return true;
    }

}
