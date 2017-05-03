package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to show clients (probably filtered somehow).
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowClients extends ClinicServlet {

    /**
     * {@inheritDoc}
     */
    public ShowClients() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    ShowClients(final ClinicService clinicService, final RoleService roleService) {
        super(clinicService, roleService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        super.doGet(request, response);

        final String filterType = request.getParameter("filterType");
        final String filterName = request.getParameter("filterName");
        Client[] clients;
        if (filterType != null && filterType.equals("client") && filterName != null && !filterName.isEmpty()) {
            try {
                clients = new Client[]{this.clinicService.findClientByName(filterName)};
            } catch(ServiceException e) {
                clients = new Client[]{};
            }
        } else if (filterType != null && filterType.equals("pet") && filterName != null && !filterName.isEmpty()) {
            clients = this.clinicService.findClientsByPetName(filterName);
        } else {
            clients = this.clinicService.getAllClients();
        }

        request.setAttribute("clients", clients);
        request.getRequestDispatcher("/WEB-INF/view/ShowClients.jsp").forward(request, response);
    }
}
