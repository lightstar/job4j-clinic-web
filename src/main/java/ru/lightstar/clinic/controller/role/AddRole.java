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
 * <code>AddRole</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/role/add")
public class AddRole extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public AddRole(final ClinicService clinicService, final DrugService drugService,
                   final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Handle add role request.
     *
     * @param name role's name.
     * @param redirectAttributes redirect attributes object.
     * @param request user's request.
     * @return view name.
     * @throws ServiceException thrown if can't add role.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String addRole(@RequestParam("name") final String name, final RedirectAttributes redirectAttributes,
                          final HttpServletRequest request)
            throws ServiceException {
        this.roleService.addRole(name);
        this.setMessage(redirectAttributes,  "Role added");
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
