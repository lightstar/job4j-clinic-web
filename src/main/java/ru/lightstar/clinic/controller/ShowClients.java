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
import ru.lightstar.clinic.security.SecurityUtil;

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
        if (SecurityUtil.getAuthRole().equals("ROLE_CLIENT")) {
            model.addAttribute("noFilter", true);
        }

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

        model.addAttribute("clients", this.filterPermittedClients(clients));
        return "ShowClients";
    }

    /**
     * Count clients with permitted access in array.
     *
     * @param clients clients array.
     * @return count of clients with permitted access.
     */
    private int countPermittedClients(final Client[] clients) {
        int countPermittedClients = 0;
        for (final Client client : clients) {
            if (this.isAccessToClientPermitted(client)) {
                countPermittedClients++;
            }
        }
        return countPermittedClients;
    }

    /**
     * Filter clients array leaving clients with permitted access only.
     *
     * @param clients clients array.
     * @return array of clients with permitted access.
     */
    private Client[] filterPermittedClients(final Client[] clients) {
        final Client[] permittedClients = new Client[this.countPermittedClients(clients)];

        int index = 0;
        for (final Client client : clients) {
            if (this.isAccessToClientPermitted(client)) {
                permittedClients[index++] = client;
            }
        }

        return permittedClients;
    }
}
