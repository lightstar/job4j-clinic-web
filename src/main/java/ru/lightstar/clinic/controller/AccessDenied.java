package ru.lightstar.clinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <code>AccessDenied</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/access-denied")
public class AccessDenied {

    /**
     * Show access denied page.
     *
     * @param model model that will be sent to view.
     * @return view name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showForm(final ModelMap model) {
        model.addAttribute("error","Access to this page is denied!");
        return "AccessDenied";
    }
}
