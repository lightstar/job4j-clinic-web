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
 * Servlet used to add new role.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AddRole extends ClinicServlet {

    /**
     * {@inheritDoc}
     */
    public AddRole() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    AddRole(final ClinicService clinicService, final RoleService roleService) {
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
            this.roleService.addRole(request.getParameter("name"));
        } catch (NullPointerException e) {
            errorString = "Invalid request parameters";
        } catch (ServiceException e) {
            errorString = e.getMessage();
        } catch (RuntimeException e) {
            errorString = "Unknown error";
        }

        this.finishUpdate(request, response, "Role added", errorString, "/role");
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
