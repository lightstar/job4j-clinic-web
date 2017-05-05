package ru.lightstar.clinic.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

import javax.servlet.http.HttpServletRequest;

/**
 * <code>DeleteMessage</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/client/message/delete")
public class DeleteMessage extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public DeleteMessage(final ClinicService clinicService, final DrugService drugService,
                      final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Handle delete message request.
     *
     * @param name client's name.
     * @param id message's id.
     * @param request user's request.
     * @return view name.
     * @throws ServiceException thrown if can't delete message.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String deleteMessage(@RequestParam("name") final String name, @RequestParam("id") final int id,
                             final HttpServletRequest request)
            throws ServiceException {
        final Client client;
        try {
            client = this.clinicService.findClientByName(name);
        } catch (ServiceException e) {
            return "redirect:/";
        }

        this.messageService.deleteMessage(client, id);
        this.setMessage(request,  "Message deleted");
        return this.redirectToForm(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String redirectToForm(final HttpServletRequest request) {
        return "redirect:/client/message?name=" + this.getEncodedParam(request, "name");
    }
}
