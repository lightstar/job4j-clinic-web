package ru.lightstar.clinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

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
     * @param request user's request.
     * @return view name.
     * @throws ServiceException thrown if can't delete client.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String deleteClient(@RequestParam("name") final String name, final HttpServletRequest request)
            throws ServiceException {
        this.clinicService.deleteClient(name);
        this.setMessage(request,  "Client deleted");
        return this.redirectToForm(request);
    }
}
