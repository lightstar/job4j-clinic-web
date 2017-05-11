package ru.lightstar.clinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.form.SetClientPetForm;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;
import ru.lightstar.clinic.pet.Sex;

import javax.servlet.http.HttpServletRequest;

/**
 * <code>SetClientPet</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/client/pet/set")
public class SetClientPet extends ClinicController {

    /**
     * {@inheritDoc}
     */
    public SetClientPet(final ClinicService clinicService, final DrugService drugService,
                        final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Show set client's pet form.
     *
     * @param name client's name.
     * @return view name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showForm(@RequestParam final String name) {
        if (this.getClientFromNameParam(name) == Client.NONE) {
            return "redirect:/";
        }

        return "SetClientPet";
    }

    /**
     * Handle set client's pet request.
     *
     * @param form form filled by user.
     * @param redirectAttributes redirect attributes object.
     * @return view name.
     * @throws ServiceException thrown if can't set client's pet.
     * @throws NameException thrown if name is invalid.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String setClientPet(@ModelAttribute final SetClientPetForm form,
                               final RedirectAttributes redirectAttributes)
            throws ServiceException, NameException {
        if (this.getClientFromNameParam(form.getName()) == Client.NONE) {
            return "redirect:/";
        }

        this.clinicService.setClientPet(form.getName(), form.getPetType(), form.getPetName(), form.getPetAge(),
                form.getPetSex().toLowerCase().equals("m") ? Sex.M : Sex.F);
        this.setMessage(redirectAttributes,  "Pet was set");
        return "redirect:/";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String redirectToForm(final HttpServletRequest request) {
        return "redirect:/client/pet/set?name=" + this.getEncodedParam(request, "name");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setEnteredFormValues(final RedirectAttributes redirectAttributes,
                                        final HttpServletRequest request) {
        this.addFlashAttributeFromRequestParam(redirectAttributes, request, "petType");
        this.addFlashAttributeFromRequestParam(redirectAttributes, request, "petName");
        this.addFlashAttributeFromRequestParam(redirectAttributes, request, "petAge");
        this.addFlashAttributeFromRequestParam(redirectAttributes, request, "petSex");
    }
}
