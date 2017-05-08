package ru.lightstar.clinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Base class for all clinic controllers.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
public abstract class ClinicController {

    /**
     * Clinic service bean.
     */
    protected final ClinicService clinicService;

    /**
     * Drug service bean.
     */
    protected final DrugService drugService;

    /**
     * Role service bean.
     */
    protected final RoleService roleService;

    /**
     * Message service bean.
     */
    protected final MessageService messageService;

    /**
     * Constructs <code>ClinicController</code> object.
     *
     * @param clinicService clinic service bean.
     * @param drugService drug service bean.
     * @param roleService role service bean.
     * @param messageService message service bean.
     */
    public ClinicController(final ClinicService clinicService, final DrugService drugService,
                            final RoleService roleService, final MessageService messageService) {
        this.clinicService = clinicService;
        this.drugService = drugService;
        this.roleService = roleService;
        this.messageService = messageService;
    }

    /**
     * Service exceptions handler.
     *
     * @param exception exception object.
     * @param redirectAttributes redirect attributes object.
     * @param request user's request.
     * @return view name.
     */
    @ExceptionHandler({ServiceException.class, NameException.class})
    public String serviceException(final Exception exception, final RedirectAttributes redirectAttributes,
                                   final HttpServletRequest request) {
        this.setError(redirectAttributes, exception.getMessage());
        this.setEnteredFormValues(redirectAttributes, request);
        return this.redirectToForm(request);
    }

    /**
     * Binding exceptions handler.
     *
     * @param redirectAttributes redirect attributes object.
     * @param request user's request.
     * @return view name.
     */
    @ExceptionHandler({ServletRequestBindingException.class, BindException.class})
    public String bindException(final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        this.setError(redirectAttributes, "Invalid parameters");
        this.setEnteredFormValues(redirectAttributes, request);
        return this.redirectToForm(request);
    }

    /**
     * Redirect to page with form. Usually called on error.
     *
     * @param request user's request.
     * @return view name.
     */
    protected String redirectToForm(final HttpServletRequest request) {
        return "redirect:/";
    }

    /**
     * Get encoded request parameter. Returns empty string if parameter does not exists.
     *
     * @param request user's request.
     * @param name parameter's name.
     * @return encoded parameter's value.
     */
    protected String getEncodedParam(final HttpServletRequest request, final String name) {
        if (!request.getParameterMap().containsKey(name)) {
            return "";
        }

        try {
            return URLEncoder.encode(request.getParameter(name), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set error message in redirect attributes.
     *
     * @param redirectAttributes redirect attributes object.
     * @param message error message.
     */
    protected void setError(final RedirectAttributes redirectAttributes, final String message) {
        redirectAttributes.addFlashAttribute("error", String.format("%s.", message));
    }

    /**
     * Set informational message in redirect attributes.
     *
     * @param redirectAttributes redirect attributes object.
     * @param message message.
     */
    protected void setMessage(final RedirectAttributes redirectAttributes, final String message) {
        redirectAttributes.addFlashAttribute("message", String.format("%s.", message));
    }

    /**
     * Set entered form values from request parameters into flash redirect attributes.
     *
     * @param redirectAttributes redirect attributes object.
     * @param request user's request.
     */
    protected void setEnteredFormValues(final RedirectAttributes redirectAttributes,
                                        final HttpServletRequest request) {
    }

    /**
     * Add attribute to model only if it is not exists here already.
     *
     * @param model request's model object.
     * @param name attribute's name.
     * @param value attribute's value.
     */
    protected void addToModelIfAbsent(final ModelMap model, final String name, final String value) {
        if (!model.containsAttribute(name)) {
            model.addAttribute(name, value);
        }
    }

    /**
     * Add flash redirect attribute using according request parameter.
     *
     * @param redirectAttributes redirect attributes object.
     * @param request user's request.
     * @param name attribute's name.
     */
    protected void addFlashAttributeFromRequestParam(final RedirectAttributes redirectAttributes,
                                                     final HttpServletRequest request, final String name) {
        if (request.getParameterMap().containsKey(name)) {
            redirectAttributes.addFlashAttribute(name, request.getParameter(name));
        }
    }
}