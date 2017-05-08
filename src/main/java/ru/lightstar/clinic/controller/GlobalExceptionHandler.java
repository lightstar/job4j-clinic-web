package ru.lightstar.clinic.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller that globally handles exceptions in all other controllers.
 *
 * @author LightStar
 * @since 0.0.1
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Data access exception handler.
     *
     * @param redirectAttributes redirect attributes object.
     * @return view name.
     */
    @ExceptionHandler(DataAccessException.class)
    public String dataAccessExceptionHandler(final RedirectAttributes redirectAttributes) {
        return this.exceptionHandler(redirectAttributes, "Database error");
    }

    /**
     * Default exception handler.
     *
     * @param redirectAttributes redirect attributes object.
     * @return view name.
     */
    @ExceptionHandler(RuntimeException.class)
    public String defaultExceptionHandler(final RedirectAttributes redirectAttributes) {
        return this.exceptionHandler(redirectAttributes, "Unknown error");

    }

    /**
     * Generic exception handler.
     *
     * @param redirectAttributes redirect attributes object.
     * @param errorMessage error's message.
     * @return view name.
     */
    private String exceptionHandler(final RedirectAttributes redirectAttributes, final String errorMessage) {
        redirectAttributes.addFlashAttribute("error", String.format("%s.", errorMessage));
        return "redirect:/error";
    }
}
