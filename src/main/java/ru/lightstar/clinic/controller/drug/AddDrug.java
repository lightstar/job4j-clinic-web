package ru.lightstar.clinic.controller.drug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.controller.ClinicController;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.http.HttpServletRequest;

/**
 * <code>AddDrug</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/drug/add")
public class AddDrug extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public AddDrug(final ClinicService clinicService, final DrugService drugService,
                     final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Handle add drug request.
     *
     * @param name drug's name.
     * @param request user's request.
     * @return view name.
     * @throws ServiceException thrown if can't add drug.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String addDrug(@RequestParam("name") final String name, final HttpServletRequest request)
            throws ServiceException {
        this.drugService.addDrug(name);
        this.setMessage(request,  "Drug added");
        return this.redirectToForm(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String redirectToForm(final HttpServletRequest request) {
        return "redirect:/drug";
    }
}
