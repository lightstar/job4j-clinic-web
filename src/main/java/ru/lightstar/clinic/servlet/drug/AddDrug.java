package ru.lightstar.clinic.servlet.drug;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to add new drug.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AddDrug extends DrugServlet {

    /**
     * {@inheritDoc}
     */
    public AddDrug() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    AddDrug(final ClinicService clinicService, final RoleService roleService,
            final DrugService drugService) {
        super(clinicService, roleService, drugService);
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
            this.drugService.addDrug(request.getParameter("name"));
        } catch (NullPointerException e) {
            errorString = "Invalid request parameters";
        } catch (ServiceException e) {
            errorString = e.getMessage();
        } catch (RuntimeException e) {
            errorString = "Unknown error";
        }

        this.finishUpdate(request, response, "Drug added", errorString, "/drug");
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
