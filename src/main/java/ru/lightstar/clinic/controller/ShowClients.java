package ru.lightstar.clinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.form.FilterForm;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

/**
 * <code>ShowClients</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/")
public class ShowClients extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public ShowClients(final ClinicService clinicService, final DrugService drugService,
                       final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Show main page that displays list of all clients in clinic.
     * Also some filters may be used.
     *
     * @param filter filter form filled by user.
     * @param model model that will be sent to view.
     * @return view name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String show(@ModelAttribute final FilterForm filter, final ModelMap model) {
        Client[] clients = null;

        if (!filter.isEmpty()) {
            switch (filter.getFilterType()) {
                case "client":
                    try {
                        clients = new Client[]{this.clinicService.findClientByName(filter.getFilterName())};
                    } catch (ServiceException e) {
                        clients = new Client[]{};
                    }
                    break;
                case "pet":
                    clients = this.clinicService.findClientsByPetName(filter.getFilterName());
            }
        }

        if (clients == null) {
            clients = this.clinicService.getAllClients();
        }

        model.addAttribute("clients", clients);
        return "ShowClients";
    }
}
