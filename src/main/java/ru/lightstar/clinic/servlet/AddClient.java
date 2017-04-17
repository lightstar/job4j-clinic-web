package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to add client to clinic.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AddClient extends ClinicServlet {

    /**
     * {@inheritDoc}
     */
    public AddClient() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public AddClient(final ClinicService clinicService) {
        super(clinicService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/AddClient.jsp").forward(request, response);
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
            this.clinicService.addClient(Integer.valueOf(request.getParameter("pos")) - 1,
                    request.getParameter("name"), request.getParameter("email"), request.getParameter("phone"));
        } catch (NullPointerException | NumberFormatException e) {
            errorString = "Invalid request parameters";
        } catch (NameException | ServiceException e) {
            errorString = e.getMessage();
        }

        this.finishUpdateForm(request, response, "Client added", errorString, "/");
    }

    /**
     * Check that needed parameters are not null.
     *
     * @param request user's request.
     */
    private void checkParameters(final HttpServletRequest request) {
        if (request.getParameter("pos") == null || request.getParameter("name") == null ||
                request.getParameter("email") == null || request.getParameter("phone") == null) {
            throw new NullPointerException();
        }
    }
}
