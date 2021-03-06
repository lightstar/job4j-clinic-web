package ru.lightstar.clinic.servlet.drug;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.RoleService;
import ru.lightstar.clinic.pet.Pet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to give drug to some pet.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class GiveDrug extends DrugServlet {

    /**
     * {@inheritDoc}
     */
    public GiveDrug() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    GiveDrug(final ClinicService clinicService, final RoleService roleService,
             final DrugService drugService) {
        super(clinicService, roleService, drugService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        super.doGet(request, response);

        this.setAttributeFromParameter(request, "clientName", "");

        request.getRequestDispatcher("/WEB-INF/view/GiveDrug.jsp").forward(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        String errorString = "";
        String messageString = "";
        try {
            this.checkParameters(request);
            final Pet pet = this.clinicService.getClientPet(request.getParameter("clientName"));
            final Drug drug = this.drugService.takeDrug(request.getParameter("name"));
            messageString = String.format("Gave %s to %s", drug, pet);
        } catch (NullPointerException e) {
            errorString = "Invalid request parameters";
        } catch (ServiceException e) {
            errorString = e.getMessage();
        } catch (RuntimeException e) {
            errorString = "Unknown error";
        }

        this.finishUpdateForm(request, response, messageString, errorString, "/drug");
    }

    /**
     * Check that needed parameters are not null.
     *
     * @param request user's request.
     */
    private void checkParameters(final HttpServletRequest request) {
        if (request.getParameter("name") == null || request.getParameter("clientName") == null) {
            throw new NullPointerException();
        }
    }
}
