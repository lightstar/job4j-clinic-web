package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;

import javax.servlet.ServletException;

/**
 * Base class for all drug servlets.
 *
 * @author LightStar
 * @since 0.0.1
 */
public abstract class DrugServlet extends ClinicServlet {

    /**
     * Global drug service used by all servlets. Usually it is set in {@link #init} method but in tests it is
     * provided in constructor.
     */
    protected DrugService drugService = null;

    /**
     * Constructs <code>DrugServlet</code> object.
     */
    public DrugServlet() {
        super();
    }

    /**
     * Constructs <code>DrugServlet</code> object using pre-defined clinic and drug services (used in tests).
     *
     * @param clinicService pre-defined clinic service.
     * @param drugService pre-defined drug service.
     */
    DrugServlet(final ClinicService clinicService, final DrugService drugService) {
        super(clinicService);
        this.drugService = drugService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.drugService = (DrugService) this.getServletContext().getAttribute("drugService");
    }
}
