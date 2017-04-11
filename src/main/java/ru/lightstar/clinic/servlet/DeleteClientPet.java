package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.store.ClinicCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to delete client's pet.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class DeleteClientPet extends HttpServlet {

    /**
     * Global clinic service used by all servlets.
     */
    private final ClinicService clinicService = ClinicCache.getService();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        String errorString = "";
        try {
            this.clinicService.deleteClientPet(request.getParameter("name"));
        } catch (NullPointerException | NumberFormatException e) {
            errorString = "Invalid request parameters";
        } catch (ServiceException e) {
            errorString = e.getMessage();
        }

        if (errorString.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            request.setAttribute("error", errorString);
            request.getRequestDispatcher("/").forward(request, response);
        }
    }
}
