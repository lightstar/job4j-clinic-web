package ru.lightstar.clinic.servlet.drug;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to show list of drugs.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowDrugs extends DrugServlet {

    /**
     * {@inheritDoc}
     */
    public ShowDrugs() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    ShowDrugs(final ClinicService clinicService, final RoleService roleService,
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
        request.setAttribute("drugs", this.drugService.getAllDrugs());
        request.getRequestDispatcher("/WEB-INF/view/ShowDrugs.jsp").forward(request, response);
    }
}
