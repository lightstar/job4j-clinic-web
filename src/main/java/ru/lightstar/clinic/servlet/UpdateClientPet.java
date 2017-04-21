package ru.lightstar.clinic.servlet;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.RoleService;
import ru.lightstar.clinic.pet.Pet;
import ru.lightstar.clinic.pet.Sex;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to update client pet's name, age and sex.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class UpdateClientPet extends ClinicServlet {

    /**
     * {@inheritDoc}
     */
    public UpdateClientPet() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    UpdateClientPet(final ClinicService clinicService, final RoleService roleService) {
        super(clinicService, roleService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final Pet pet;
        try {
            final String name = request.getParameter("name");
            if (name == null) {
                throw new NullPointerException();
            }
            pet = this.clinicService.getClientPet(name);
        } catch (ServiceException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        this.setAttributeFromParameter(request, "newName", pet.getName());
        this.setAttributeFromParameter(request, "newAge", String.valueOf(pet.getAge()));
        this.setAttributeFromParameter(request, "newSex", pet.getSex() == Sex.M ? "m" : "f");
        request.getRequestDispatcher("/WEB-INF/view/UpdateClientPet.jsp").forward(request, response);
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
            this.clinicService.updateClientPet(request.getParameter("name"),
                    request.getParameter("newName"),
                    Integer.valueOf(request.getParameter("newAge")),
                    request.getParameter("newSex").toLowerCase().equals("m") ? Sex.M : Sex.F);
        } catch (NullPointerException | NumberFormatException e) {
            errorString = "Invalid request parameters";
        } catch (NameException | ServiceException e) {
            errorString = e.getMessage();
        }

        this.finishUpdateForm(request, response, "Pet updated", errorString, "/");

    }

    /**
     * Check that needed parameters are not null.
     *
     * @param request user's request.
     */
    private void checkParameters(final HttpServletRequest request) {
        if (request.getParameter("name") == null || request.getParameter("newName") == null ||
                request.getParameter("newAge") == null || request.getParameter("newSex") == null) {
            throw new NullPointerException();
        }
    }
}
