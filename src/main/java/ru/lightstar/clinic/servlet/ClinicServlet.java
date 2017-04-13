package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.ClinicService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

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
}
