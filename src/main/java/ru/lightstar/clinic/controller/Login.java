package ru.lightstar.clinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <code>Login</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/login")
public class Login {

    /**
     * Show login form.
     *
     * @param error if this parameter exists then login error occurred.
     * @param logout if this parameter exists then logout occurred.
     * @param model model that will be sent to view.
     * @return view name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showForm(@RequestParam(required = false) final String error,
                           @RequestParam(required = false) final String logout,
                           final ModelMap model) {
        if (error != null) {
            model.addAttribute("error","Wrong login or password!");
        } else if (logout != null) {
            model.addAttribute("message","Logout successful.");
        }

        return "Login";
    }
}
