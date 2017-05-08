package ru.lightstar.clinic.controller.drug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.controller.ClinicController;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.form.GiveDrugForm;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;
import ru.lightstar.clinic.pet.Pet;

import javax.servlet.http.HttpServletRequest;

/**
 * <code>GiveDrug</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/drug/give")
public class GiveDrug extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public GiveDrug(final ClinicService clinicService, final DrugService drugService,
                   final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Show give drug form.
     *
     * @return view name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showForm() {
        return "GiveDrug";
    }

    /**
     * Handle give drug request.
     *
     * @param form form filled by user.
     * @param redirectAttributes redirect attributes object.
     * @return view name.
     * @throws ServiceException thrown if can't give drug.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String giveDrug(@ModelAttribute GiveDrugForm form, final RedirectAttributes redirectAttributes)
            throws ServiceException {
        final Pet pet = this.clinicService.getClientPet(form.getClientName());
        final Drug drug = this.drugService.takeDrug(form.getName());
        this.setMessage(redirectAttributes,  String.format("Gave %s to %s", drug, pet));
        return "redirect:/drug";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String redirectToForm(final HttpServletRequest request) {
        return "redirect:/drug/give?name=" + this.getEncodedParam(request, "name");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setEnteredFormValues(final RedirectAttributes redirectAttributes,
                                        final HttpServletRequest request) {
        this.addFlashAttributeFromRequestParam(redirectAttributes, request, "clientName");
    }
}
