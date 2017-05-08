package ru.lightstar.clinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.form.UpdateClientPetForm;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;
import ru.lightstar.clinic.pet.Pet;
import ru.lightstar.clinic.pet.Sex;

import javax.servlet.http.HttpServletRequest;

/**
 * <code>UpdateClientPet</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/client/pet/update")
public class UpdateClientPet extends ClinicController {

    /**
     * {@inheritDoc}
     */
    public UpdateClientPet(final ClinicService clinicService, final DrugService drugService,
                        final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Show update client's pet form.
     *
     * @param name client's name.
     * @param model model that will be sent to view.
     * @return view name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showForm(@RequestParam("name") final String name, final ModelMap model) {
        final Pet pet;
        try {
            pet = this.clinicService.getClientPet(name);
        } catch (ServiceException | NullPointerException e) {
            return "redirect:/";
        }

        model.addAttribute("newName", pet.getName());
        model.addAttribute("newAge", pet.getAge());
        model.addAttribute("newSex", pet.getSex() == Sex.M ? "m" : "f");
        return "UpdateClientPet";
    }

    /**
     * Handle update client's pet request.
     *
     * @param form form filled by user.
     * @param redirectAttributes redirect attributes object.
     * @return view name.
     * @throws ServiceException thrown if can't update client's pet.
     * @throws NameException thrown if name is invalid.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String updateClientPet(@ModelAttribute final UpdateClientPetForm form,
                                  final RedirectAttributes redirectAttributes)
            throws ServiceException, NameException {
        this.clinicService.updateClientPet(form.getName(), form.getNewName(), form.getNewAge(),
                form.getNewSex().toLowerCase().equals("m") ? Sex.M : Sex.F);
        this.setMessage(redirectAttributes,  "Pet updated");
        return "redirect:/";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String redirectToForm(HttpServletRequest request) {
        return "redirect:/client/pet/update?name=" + this.getEncodedParam(request, "name");
    }
}
