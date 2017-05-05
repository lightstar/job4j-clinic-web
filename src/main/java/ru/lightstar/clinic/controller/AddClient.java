package ru.lightstar.clinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.form.AddClientForm;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.http.HttpServletRequest;

/**
 * <code>AddClient</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/client/add")
public class AddClient extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public AddClient(final ClinicService clinicService, final DrugService drugService,
                     final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Show add client form.
     *
     * @param model model that will be sent to view.
     * @return view name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showForm(final ModelMap model) {
        model.addAttribute("roles", this.roleService.getAllRoles());
        return "AddClient";
    }

    /**
     * Handle add client request.
     *
     * @param form form filled by user.
     * @param request user's request.
     * @return view name.
     * @throws ServiceException thrown if can't add client.
     * @throws NameException thrown if client's name is invalid.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String addClient(@ModelAttribute AddClientForm form, final HttpServletRequest request)
            throws ServiceException, NameException {
        final Role role = this.roleService.getRoleByName(form.getRole());
        this.clinicService.addClient(form.getPos() - 1, form.getName(), form.getEmail(), form.getPhone(),
                role);
        this.setMessage(request,  "Client added");
        return "redirect:/";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String redirectToForm(final HttpServletRequest request) {
        return "redirect:/client/add?pos=" + this.getEncodedParam(request, "pos");
    }
}
