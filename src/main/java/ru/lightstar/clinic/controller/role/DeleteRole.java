package ru.lightstar.clinic.controller.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.controller.ClinicController;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.http.HttpServletRequest;

/**
 * <code>DeleteRole</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/role/delete")
public class DeleteRole extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public DeleteRole(final ClinicService clinicService, final DrugService drugService,
                   final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Handle delete role request.
     *
     * @param name role's name.
     * @param redirectAttributes redirect attributes object.
     * @param request user's request.
     * @return view name.
     * @throws ServiceException thrown if can't delete role.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String deleteRole(@RequestParam("name") final String name, final RedirectAttributes redirectAttributes,
                             final HttpServletRequest request)
            throws ServiceException {
        this.roleService.deleteRole(name);
        this.setMessage(redirectAttributes,  "Role deleted");
        return this.redirectToForm(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String redirectToForm(final HttpServletRequest request) {
        return "redirect:/role";
    }
}
