package ru.lightstar.clinic.servlet.role;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.persistence.RoleService;
import ru.lightstar.clinic.servlet.ClinicServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to show all client roles.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowRoles extends ClinicServlet {

    /**
     * {@inheritDoc}
     */
    public ShowRoles() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    ShowRoles(final ClinicService clinicService, final RoleService roleService) {
        super(clinicService, roleService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("roles", this.roleService.getAllRoles());
        request.getRequestDispatcher("/WEB-INF/view/ShowRoles.jsp").forward(request, response);
    }
}
