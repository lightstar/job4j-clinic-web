package ru.lightstar.clinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.form.FilterForm;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

/**
 * <code>ShowPets</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/client/pet")
public class ShowPets extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public ShowPets(final ClinicService clinicService, final DrugService drugService,
                       final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Show clinic's pet list.
     *
     * @param model model that will be sent to view.
     * @return view name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String show(@ModelAttribute final FilterForm filter, final ModelMap model) {
        model.addAttribute("pets", this.clinicService.getAllPets());
        return "ShowPets";
    }
}
