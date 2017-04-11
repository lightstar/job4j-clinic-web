package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.Client;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.store.ClinicCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to show clients (probably filtered somehow).
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowClients extends HttpServlet {

    /**
     * Global clinic service used by all servlets.
     */
    private final ClinicService clinicService = ClinicCache.getService();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        Client[] clients;
        if (request.getParameterMap().containsKey("filterName") &&
                !request.getParameter("filterName").isEmpty()) {
            try {
                clients = new Client[]{this.clinicService.findClientByName(request.getParameter("filterName"))};
            } catch(ServiceException e) {
                clients = new Client[]{};
            }
        } else if (request.getParameterMap().containsKey("filterPetName") &&
                !request.getParameter("filterPetName").isEmpty()) {
            clients = this.clinicService.findClientsByPetName(request.getParameter("filterPetName"));
        } else {
            clients = this.clinicService.getAllClients();
        }

        request.setAttribute("clients", clients);
        request.getRequestDispatcher("/WEB-INF/view/ShowClients.jsp").forward(request, response);
    }
}
