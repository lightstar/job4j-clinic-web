package ru.lightstar.clinic.controller.message;

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
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.http.HttpServletRequest;

/**
 * <code>AddMessage</code> class.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/client/message/add")
public class AddMessage extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public AddMessage(final ClinicService clinicService, final DrugService drugService,
                        final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Handle add message request.
     *
     * @param name client's name.
     * @param text message's text.
     * @param redirectAttributes redirect attributes object.
     * @param request user's request.
     * @return view name.
     * @throws ServiceException thrown if can't add message.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String addMessage(@RequestParam final String name,
                             @RequestParam final String text,
                             final RedirectAttributes redirectAttributes,
                             final HttpServletRequest request)
            throws ServiceException{
        final Client client = this.getClientFromNameParam(name);
        if (client == Client.NONE) {
            return "redirect:/";
        }

        this.messageService.addMessage(client, text);
        this.setMessage(redirectAttributes,  "Message added");
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
