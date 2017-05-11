package ru.lightstar.clinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;
import ru.lightstar.clinic.security.SecurityUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * <code>DeleteClient</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/client/delete")
public class DeleteClient extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public DeleteClient(final ClinicService clinicService, final DrugService drugService,
                           final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Handle delete client request.
     *
     * @param name client's name.
     * @param redirectAttributes redirect attributes object.
     * @param request user's request.
     * @return view name.
     * @throws ServiceException thrown if can't delete client.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String deleteClient(@RequestParam final String name, final RedirectAttributes redirectAttributes,
                               final HttpServletRequest request)
            throws ServiceException {
        if (name.equals(SecurityUtil.getAuthName()) || this.getClientFromNameParam(name) == Client.NONE) {
            return "redirect:/";
        }

        this.clinicService.deleteClient(name);
        this.setMessage(redirectAttributes,  "Client deleted");
        return this.redirectToForm(request);
    }
}
