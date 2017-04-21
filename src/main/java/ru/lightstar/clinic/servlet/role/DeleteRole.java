package ru.lightstar.clinic.servlet.role;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.RoleService;
import ru.lightstar.clinic.servlet.ClinicServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to delete role.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class DeleteRole extends ClinicServlet {

    /**
     * {@inheritDoc}
     */
    public DeleteRole() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    DeleteRole(final ClinicService clinicService, final RoleService roleService) {
        super(clinicService, roleService);
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
            this.roleService.deleteRole(request.getParameter("name"));
        } catch (ServiceException e) {
            errorString = e.getMessage();
        }

        this.finishUpdate(request, response, "Role deleted", errorString, "/role");
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
