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
 * Servlet used to add client to clinic.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AddClient extends HttpServlet {

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
            this.clinicService.addClient(Integer.valueOf(request.getParameter("pos")) - 1,
                    request.getParameter("name"));
        } catch (NullPointerException | NumberFormatException e) {
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
}