package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Base class for all clinic servlets.
 *
 * @author LightStar
 * @since 0.0.1
 */
public abstract class ClinicServlet extends HttpServlet {

    /**
     * Global prefix in url for all servlets.
     */
    private static final String PREFIX = "/servlets";

    /**
     * Global clinic service used by all servlets. Usually it is set in {@link #init} method but in tests it is
     * provided in constructor.
     */
    protected ClinicService clinicService = null;

    /**
     * Global role service used by all servlets. Usually it is set in {@link #init} method but in tests it is
     * provided in constructor.
     */
    protected RoleService roleService = null;

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
     * @param roleService pre-defined role service.
     */
    protected ClinicServlet(final ClinicService clinicService, final RoleService roleService) {
        super();
        this.clinicService = clinicService;
        this.roleService = roleService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.clinicService = (ClinicService) this.getServletContext().getAttribute("clinicService");
        this.roleService = (RoleService) this.getServletContext().getAttribute("roleService");
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("prefix", PREFIX);
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

    /**
     * Finish update request, redirecting to either <code>doGet</code> method on error (which shows form again),
     * or to specified location on success.
     *
     * @param request user's request
     * @param response response for user
     * @param messageString message string on success.
     * @param errorString message string on error.
     * @param redirectUrl redirect url on success.
     * @throws ServletException servlet processing exception.
     * @throws IOException i/o exception.
     */
    protected void finishUpdateForm(final HttpServletRequest request, final HttpServletResponse response,
                                    final String messageString, final String errorString, final String redirectUrl)
            throws ServletException, IOException {
        if (!errorString.isEmpty()) {
            request.getSession().setAttribute("error", String.format("%s.", errorString));
            this.doGet(request, response);
        } else {
            request.getSession().setAttribute("message",  String.format("%s.", messageString));
            response.sendRedirect(request.getContextPath() + PREFIX + redirectUrl);
        }
    }

    /**
     * Finish update request, redirecting to specified location.
     *
     * @param request user's request
     * @param response response for user
     * @param messageString message string on success.
     * @param errorString message string on error.
     * @param redirectUrl redirect url on success.
     * @throws IOException i/o exception.
     */
    protected void finishUpdate(final HttpServletRequest request, final HttpServletResponse response,
                                final String messageString, final String errorString, final String redirectUrl)
            throws IOException {
        if (!errorString.isEmpty()) {
            request.getSession().setAttribute("error", String.format("%s.", errorString));
        } else {
            request.getSession().setAttribute("message", String.format("%s.", messageString));
        }

        response.sendRedirect(request.getContextPath() + PREFIX + redirectUrl);
    }

    /**
     * Set request's <code>roles</code> attribute or current session's error attribute if roles can't be loaded.
     *
     * @param request user's request.
     */
    protected void setRolesAttribute(final HttpServletRequest request) {
        try {
            request.setAttribute("roles", this.roleService.getAllRoles());
        } catch (RuntimeException e) {
            request.getSession().setAttribute("error", "Unknown error.");
        }
    }

    /**
     * Get client by request's <code>name</code> parameter.
     * If client can't be found then redirect to home page is performed.
     *
     * @param request user's request.
     * @param response response for user
     * @return found client.
     */
    protected Client getClientByNameParameterOrGoHome(final HttpServletRequest request,
                                                      final HttpServletResponse response)
            throws IOException {
        try {
            final String name = request.getParameter("name");
            if (name == null) {
                throw new NullPointerException();
            }
            return this.clinicService.findClientByName(name);
        } catch (ServiceException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + PREFIX + "/");
            return Client.NONE;
        }
    }
}
