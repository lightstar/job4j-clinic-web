package ru.lightstar.clinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;

/**
 * <code>ShowClients</code> controller.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Controller
@RequestMapping("/")
public class ShowClients {

    /**
     * Clinic service bean.
     */
    private final ClinicService clinicService;

    /**
     * Constructs <code>ShowClients</code> controller object.
     *
     * @param clinicService autowired clinic service bean.
     */
    @Autowired
    public ShowClients(final ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    /**
     * Processing main page showing list of all clients in clinic.
     * Also some filters may be used.
     *
     * @param filter contents of filter form.
     * @param model model that will be sent to view.
     * @return view's name.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String show(@ModelAttribute final Filter filter, final ModelMap model) {
        Client[] clients = null;

        if (!filter.isEmpty()) {
            switch (filter.getFilterType()) {
                case "client":
                    try {
                        clients = new Client[]{this.clinicService.findClientByName(filter.getFilterName())};
                    } catch (ServiceException e) {
                        clients = new Client[]{};
                    }
                    break;
                case "pet":
                    clients = this.clinicService.findClientsByPetName(filter.getFilterName());
            }
        }

        if (clients == null) {
            clients = this.clinicService.getAllClients();
        }

        model.addAttribute("clients", clients);
        return "ShowClients";
    }

    /**
     * Objects of this class contains filter form parameters.
     */
    public static class Filter {

        /**
         * Filter's type.
         */
        private String filterType;

        /**
         * Filter's name value.
         */
        private String filterName;

        /**
         * Constructs <code>Filter</code> object.
         */
        public Filter() {
            this.filterType = "";
            this.filterName = "";
        }

        /**
         * Get filter's type.
         *
         * @return filter's type.
         */
        public String getFilterType() {
            return this.filterType;
        }

        /**
         * Set filter's type.
         *
         * @param filterType filter's type.
         */
        public void setFilterType(final String filterType) {
            this.filterType = filterType;
        }

        /**
         * Get filter's name value.
         *
         * @return filter's name value.
         */
        public String getFilterName() {
            return this.filterName;
        }

        /**
         * Set filter's name value.
         *
         * @param filterName filter's name value.
         */
        public void setFilterName(final String filterName) {
            this.filterName = filterName;
        }

        /**
         * Check if filter is empty.
         *
         * @return <code>true</code> if filter is empty (i.e. efficiently not present)
         * and <code>false</code> - otherwise.
         */
        public boolean isEmpty() {
            return this.filterName.isEmpty();
        }
    }
}
