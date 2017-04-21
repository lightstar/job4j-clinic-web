package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;

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
    UpdateClient(final ClinicService clinicService) {
        super(clinicService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final Client client;
        try {
            final String name= request.getParameter("name");
            if (name == null) {
                throw new NullPointerException();
            }
            client = this.clinicService.findClientByName(name);
        } catch (ServiceException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        this.setAttributeFromParameter(request, "newName", client.getName());
        this.setAttributeFromParameter(request, "newEmail", client.getEmail());
        this.setAttributeFromParameter(request, "newPhone", client.getPhone());
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
            this.clinicService.updateClient(request.getParameter("name"), request.getParameter("newName"),
                    request.getParameter("newEmail"), request.getParameter("newPhone"));
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
                request.getParameter("newEmail") == null || request.getParameter("newPhone") == null) {
            throw new NullPointerException();
        }
    }
}
