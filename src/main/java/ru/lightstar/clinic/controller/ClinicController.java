package ru.lightstar.clinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
     * @param request user's request.
     * @return view name.
     */
    @ExceptionHandler({ServiceException.class, NameException.class})
    public String serviceException(final Exception exception, final HttpServletRequest request) {
        this.setError(request, exception.getMessage());
        return this.redirectToForm(request);
    }

    /**
     * Binding exceptions handler.
     *
     * @param request user's request.
     * @return view name.
     */
    @ExceptionHandler({ServletRequestBindingException.class, BindException.class})
    public String bindException(final HttpServletRequest request) {
        this.setError(request, "Invalid parameters");
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
     * Set error message in session attributes.
     *
     * @param request user's request.
     * @param message error message.
     */
    protected void setError(final HttpServletRequest request, final String message) {
        request.getSession().setAttribute("error", String.format("%s.", message));
    }

    /**
     * Set informational message in session attributes.
     *
     * @param request user's request.
     * @param message message.
     */
    protected void setMessage(final HttpServletRequest request, final String message) {
        request.getSession().setAttribute("message", String.format("%s.", message));
    }
}
