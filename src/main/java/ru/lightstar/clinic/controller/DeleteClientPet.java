package ru.lightstar.clinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.http.HttpServletRequest;

/**
 * <code>DeleteClientPet</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/client/pet/delete")
public class DeleteClientPet extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public DeleteClientPet(final ClinicService clinicService, final DrugService drugService,
                     final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Handle delete client's pet request.
     *
     * @param name client's name.
     * @param redirectAttributes redirect attributes object.
     * @param request user's request.
     * @return view name.
     * @throws ServiceException thrown if can't delete client's pet.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String deleteClientPet(@RequestParam("name") final String name, final RedirectAttributes redirectAttributes,
                                  final HttpServletRequest request)
            throws ServiceException {
        this.clinicService.deleteClientPet(name);
        this.setMessage(redirectAttributes,  "Pet deleted");
        return this.redirectToForm(request);
    }
}
