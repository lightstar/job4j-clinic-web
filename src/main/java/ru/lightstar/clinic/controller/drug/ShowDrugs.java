package ru.lightstar.clinic.controller.drug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.controller.ClinicController;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

/**
 * <code>ShowDrugs</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/drug")
public class ShowDrugs extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public ShowDrugs(final ClinicService clinicService, final DrugService drugService,
                    final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Show clinic's drug list.
     *
     * @param model model that will be sent to view.
     * @return view name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String show(final ModelMap model) {
        model.addAttribute("drugs", this.drugService.getAllDrugs());
        return "ShowDrugs";
    }
}
