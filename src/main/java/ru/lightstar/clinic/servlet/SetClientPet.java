package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.RoleService;
import ru.lightstar.clinic.pet.Sex;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to set client's pet.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class SetClientPet extends ClinicServlet {

    /**
     * {@inheritDoc}
     */
    public SetClientPet() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    SetClientPet(final ClinicService clinicService, final RoleService roleService) {
        super(clinicService, roleService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/SetClientPet.jsp").forward(request, response);
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
            this.clinicService.setClientPet(request.getParameter("name"), request.getParameter("petType"),
                    request.getParameter("petName"), Integer.valueOf(request.getParameter("petAge")),
                    request.getParameter("petSex").toLowerCase().equals("m") ? Sex.M : Sex.F);
        } catch (NullPointerException | NumberFormatException e) {
            errorString = "Invalid request parameters";
        } catch (NameException | ServiceException e) {
            errorString = e.getMessage();
        }

        this.finishUpdateForm(request, response, "Pet was set", errorString, "/");
    }

    /**
     * Check that needed parameters are not null.
     *
     * @param request user's request.
     */
    private void checkParameters(final HttpServletRequest request) {
        if (request.getParameter("name") == null || request.getParameter("petType") == null ||
                request.getParameter("petName") == null || request.getParameter("petAge") == null ||
                request.getParameter("petSex") == null) {
            throw new NullPointerException();
        }
    }
}
