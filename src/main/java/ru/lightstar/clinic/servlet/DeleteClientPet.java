package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to delete client's pet.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class DeleteClientPet extends ClinicServlet {

    /**
     * {@inheritDoc}
     */
    public DeleteClientPet() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    DeleteClientPet(final ClinicService clinicService) {
        super(clinicService);
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
            this.clinicService.deleteClientPet(request.getParameter("name"));
        } catch (NullPointerException e) {
            errorString = "Invalid request parameters";
        } catch (ServiceException e) {
            errorString = e.getMessage();
        }

        this.finishUpdate(request, response, "Pet deleted", errorString, "/");
    }

    /**
     * Check that needed parameters are not null.
     *
     * @param request user's request.
     */
    private void checkParameters(final HttpServletRequest request) {
        if (request.getParameter("name") == null) {
            throw new NullPointerException();
        }
    }
}
