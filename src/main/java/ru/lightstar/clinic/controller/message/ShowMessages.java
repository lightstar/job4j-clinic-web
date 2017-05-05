package ru.lightstar.clinic.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.controller.ClinicController;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

/**
 * <code>ShowMessages</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/client/message")
public class ShowMessages extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public ShowMessages(final ClinicService clinicService, final DrugService drugService,
                     final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Show client's messages.
     *
     * @param model model that will be sent to view.
     * @return view name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String show(@RequestParam("name") final String name, final ModelMap model) {
        final Client client;
        try {
            client = this.clinicService.findClientByName(name);
        } catch (ServiceException e) {
            return "redirect:/";
        }

        model.addAttribute("messages", this.messageService.getClientMessages(client));
        return "ShowMessages";
    }
}
