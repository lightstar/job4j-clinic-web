package ru.lightstar.clinic.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

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
     * @param request user's request.
     * @return view name.
     */
    @ExceptionHandler(DataAccessException.class)
    public String dataAccessExceptionHandler(final HttpServletRequest request) {
        return this.exceptionHandler(request, "Database error");
    }

    /**
     * Default exception handler.
     *
     * @param request user's request.
     * @return view name.
     */
    @ExceptionHandler(RuntimeException.class)
    public String defaultExceptionHandler(final HttpServletRequest request) {
        return this.exceptionHandler(request, "Unknown error");

    }

    /**
     * Generic exception handler.
     *
     * @param request user's request.
     * @param errorMessage error's message.
     * @return view name.
     */
    private String exceptionHandler(final HttpServletRequest request, final String errorMessage) {
        request.getSession().setAttribute("error", String.format("%s.", errorMessage));
        return "redirect:/error";
    }
}
