package ru.lightstar.clinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

/**
 * <code>Login</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/login")
public class Login  extends ClinicController {

    /**
     * {@inheritDoc}
     */
    @Autowired
    public Login(final ClinicService clinicService, final DrugService drugService,
                       final RoleService roleService, final MessageService messageService) {
        super(clinicService, drugService, roleService, messageService);
    }

    /**
     * Show login form.
     *
     * @return view name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showForm() {
        return "Login";
    }
}
