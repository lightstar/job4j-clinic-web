package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.store.ClinicCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to update client pet's name.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class UpdateClientPetName extends HttpServlet {

    /**
     * Global clinic service used by all servlets.
     */
    private final ClinicService clinicService;

    /**
     * Constructs <code>UpdateClientPetName</code> servlet.
     */
    public UpdateClientPetName() {
        this(ClinicCache.getService());
    }

    /**
     * Constructs <code>UpdateClientPetName</code> servlet using pre-defined clinic service (used in tests).
     *
     * @param clinicService pre-defined clinic service.
     */
    UpdateClientPetName(final ClinicService clinicService) {
        super();
        this.clinicService = clinicService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/UpdateClientPetName.jsp").forward(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        String errorString = "";
        try {
            this.checkParameters(request);
            this.clinicService.updateClientPetName(request.getParameter("name"), request.getParameter("newName"));
        } catch (NullPointerException e) {
            errorString = "Invalid request parameters";
        } catch (NameException | ServiceException e) {
            errorString = e.getMessage();
        }

        if (errorString.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            request.setAttribute("error", errorString);
            this.doGet(request, response);
        }
    }

    /**
     * Check that needed parameters are not null.
     *
     * @param request user's request.
     */
    private void checkParameters(final HttpServletRequest request) {
        if (request.getParameter("name") == null || request.getParameter("newName") == null) {
            throw new NullPointerException();
        }
    }
}
