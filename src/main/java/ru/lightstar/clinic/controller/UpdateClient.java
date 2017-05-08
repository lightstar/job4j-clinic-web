package ru.lightstar.clinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.form.UpdateClientForm;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.http.HttpServletRequest;

/**
 * <code>UpdateClient</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/client/update")
public class UpdateClient extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public UpdateClient(final ClinicService clinicService, final DrugService drugService,
                     final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Show update client form.
     *
     * @param name client's name.
     * @param model model that will be sent to view.
     * @return view name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showForm(@RequestParam("name") final String name, final ModelMap model) {
        final Client client;
        try {
            client = this.clinicService.findClientByName(name);
        } catch (ServiceException e) {
            return "redirect:/";
        }

        model.addAttribute("roles", this.roleService.getAllRoles());
        model.addAttribute("newName", client.getName());
        model.addAttribute("newEmail", client.getEmail());
        model.addAttribute("newPhone", client.getPhone());
        model.addAttribute("newRole", client.getRole().getName());
        return "UpdateClient";
    }

    /**
     * Handle update client request.
     *
     * @param form form filled by user.
     * @param redirectAttributes redirect attributes object.
     * @return view name.
     * @throws ServiceException thrown if can't update client.
     * @throws NameException thrown if client's name is invalid.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String updateClient(@ModelAttribute final UpdateClientForm form,
                               final RedirectAttributes redirectAttributes)
            throws ServiceException, NameException {
        final Role newRole = this.roleService.getRoleByName(form.getNewRole());
        this.clinicService.updateClient(form.getName(), form.getNewName(),
                form.getNewEmail(), form.getNewPhone(), newRole);
        this.setMessage(redirectAttributes,  "Client updated");
        return "redirect:/";
    }

    /**
     * {@inheritDoc}
     */
    protected String redirectToForm(final HttpServletRequest request) {
        return "redirect:/client/update?name=" + this.getEncodedParam(request, "name");
    }
}
