package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to show list of pets.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowPets extends ClinicServlet {

    /**
     * {@inheritDoc}
     */
    public ShowPets() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    ShowPets(final ClinicService clinicService, final RoleService roleService) {
        super(clinicService, roleService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        super.doGet(request, response);

        request.setAttribute("pets", this.clinicService.getAllPets());
        request.getRequestDispatcher("/WEB-INF/view/ShowPets.jsp").forward(request, response);
    }
}
