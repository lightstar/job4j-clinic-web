package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.ClinicService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Base class for all clinic servlets.
 *
 * @author LightStar
 * @since 0.0.1
 */
public abstract class ClinicServlet extends HttpServlet {

    /**
     * Global clinic service used by all servlets. Usually it is set in {@link #init} method but in tests it is
     * provided in constructor.
     */
    protected ClinicService clinicService = null;

    /**
     * Constructs <code>ClinicServlet</code> object.
     */
    public ClinicServlet() {
        super();
    }

    /**
     * Constructs <code>ClinicServlet</code> object using pre-defined clinic service (used in tests).
     *
     * @param clinicService pre-defined clinic service.
     */
    ClinicServlet(final ClinicService clinicService) {
        super();
        this.clinicService = clinicService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.clinicService = (ClinicService) this.getServletContext().getAttribute("clinicService");
    }

    /**
     * Set given request attribute to the corresponding request parameter with some default value if it is null.
     *
     * @param request user's request.
     * @param name attribute's name.
     * @param defaultValue attribute's default value.
     */
    protected void setAttributeFromParameter(final HttpServletRequest request, final String name,
                                             final String defaultValue) {
        if (request.getParameter(name) != null) {
            request.setAttribute(name, request.getParameter(name));
        } else {
            request.setAttribute(name, defaultValue);
        }
    }
}
