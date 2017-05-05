package ru.lightstar.clinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <code>ShowError</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/error")
public class ShowError {

    /**
     * Show error message.
     *
     * @return view name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showError() {
        return "ShowError";
    }
}
