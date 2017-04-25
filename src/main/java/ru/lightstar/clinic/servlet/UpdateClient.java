package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to update client's name, email and phone.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class UpdateClient extends ClinicServlet {

    /**
     * {@inheritDoc}
     */
    public UpdateClient() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    UpdateClient(final ClinicService clinicService, final RoleService roleService) {
        super(clinicService, roleService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        final Client client = this.getClientByNameParameterOrGoHome(request, response);
        if (client == Client.NONE) {
            return;
        }

        this.setRolesAttribute(request);
        this.setAttributeFromParameter(request, "newName", client.getName());
        this.setAttributeFromParameter(request, "newEmail", client.getEmail());
        this.setAttributeFromParameter(request, "newPhone", client.getPhone());
        this.setAttributeFromParameter(request, "newRole", client.getRole().getName());

        request.getRequestDispatcher("/WEB-INF/view/UpdateClient.jsp").forward(request, response);
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
            final Role newRole = this.roleService.getRoleByName(request.getParameter("newRole"));
            this.clinicService.updateClient(request.getParameter("name"), request.getParameter("newName"),
                   request.getParameter("newEmail"), request.getParameter("newPhone"),
                   newRole);
        } catch (NullPointerException e) {
            errorString = "Invalid request parameters";
        } catch (NameException | ServiceException e) {
            errorString = e.getMessage();
        }

        this.finishUpdateForm(request, response, "Client updated", errorString, "/");
    }

    /**
     * Check that needed parameters are not null.
     *
     * @param request user's request.
     */
    private void checkParameters(final HttpServletRequest request) {
        if (request.getParameter("name") == null || request.getParameter("newName") == null ||
                request.getParameter("newEmail") == null || request.getParameter("newPhone") == null ||
                request.getParameter("newRole") == null) {
            throw new NullPointerException();
        }
    }
}
