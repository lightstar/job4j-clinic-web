package ru.lightstar.clinic;

import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.io.IteratorInput;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.pet.Sex;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Clinic servlet.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ClinicServlet extends HttpServlet {

    /**
     * Clinic size.
     */
    private static final int CLINIC_SIZE = 10;

    /**
     * Clinic service.
     */
    private final ClinicService clinicService;

    /**
     * Constructs <code>ClinicServlet</code> object.
     */
    public ClinicServlet() {
        super();
        this.clinicService = new ClinicService(new IteratorInput(), new DummyOutput(), new Clinic(CLINIC_SIZE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        final PrintWriter writer = response.getWriter();
        writer.append("<!DOCTYPE html><html>");
        writer.append("<head><title>Pet's clinic</title></head>");
        writer.append("<body>");
        writer.append(this.viewError(request));
        writer.append(this.viewMessage(request));
        writer.append(this.viewAddClientForm(request));
        writer.append(this.viewSetClientPetForm(request));
        writer.append(this.viewDelClientForm(request));
        writer.append(this.viewDelClientPetForm(request));
        writer.append(this.viewUpdateClientForm(request));
        writer.append(this.viewUpdateClientPetForm(request));
        writer.append(this.viewClients(request));
        writer.append("</body></html>");
        writer.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getParameterMap().containsKey("action")) {
            switch (request.getParameter("action")) {
                case "add":
                    this.addClient(request);
                    break;
                case "setPet":
                    this.setClientPet(request);
                    break;
                case "delClient":
                    this.deleteClient(request);
                    break;
                case "delClientPet":
                    this.deleteClientPet(request);
                    break;
                case "upClient":
                    this.updateClient(request);
                    break;
                case "upClientPet":
                    this.updateClientPet(request);
                    break;
                default:
                    request.setAttribute("error",
                            String.format("Unknown action: %s", this.escapeHTML(request.getParameter("action"))));
            }
        }

        this.doGet(request, response);
    }

    /**
     * Process add client action.
     *
     * @param request user's request.
     */
    private void addClient(final HttpServletRequest request) {
        try {
            this.clinicService.addClient(Integer.valueOf(request.getParameter("position")) - 1,
                    request.getParameter("name"), request.getParameter("email"),
                    request.getParameter("phone"), new Role(), "");
            request.setAttribute("message", "Client added");
        } catch (NullPointerException e) {
            request.setAttribute("error", "Wrong parameters");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Position must be a number");
        } catch (ServiceException | NameException e) {
            request.setAttribute("error", e.getMessage());
        }
    }

    /**
     * Process set client's pet action.
     *
     * @param request user's request.
     */
    private void setClientPet(final HttpServletRequest request) {
        try {
            this.clinicService.setClientPet(request.getParameter("name"), request.getParameter("petType"),
                    request.getParameter("petName"), Integer.valueOf(request.getParameter("petAge")),
                    request.getParameter("petSex").toLowerCase().equals("m") ? Sex.M : Sex.F);
            request.setAttribute("message", "Pet was set");
        } catch (NullPointerException e) {
            request.setAttribute("error", "Wrong parameters");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Age must be a number");
        } catch (ServiceException | NameException e) {
            request.setAttribute("error", e.getMessage());
        }
    }

    /**
     * Process delete client action.
     *
     * @param request user's request.
     */
    private void deleteClient(final HttpServletRequest request) {
        try {
            this.clinicService.deleteClient(request.getParameter("name"));
            request.setAttribute("message", "Client deleted");
        } catch (NullPointerException e) {
            request.setAttribute("error", "Wrong parameters");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }
    }

    /**
     * Process delete client's pet action.
     *
     * @param request user's request.
     */
    private void deleteClientPet(final HttpServletRequest request) {
        try {
            this.clinicService.deleteClientPet(request.getParameter("name"));
            request.setAttribute("message", "Client's pet deleted");
        } catch (NullPointerException e) {
            request.setAttribute("error", "Wrong parameters");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }
    }

    /**
     * Process update client's name, email and phone action.
     *
     * @param request user's request.
     */
    private void updateClient(final HttpServletRequest request) {
        try {
            this.clinicService.updateClient(request.getParameter("name"), request.getParameter("newName"),
                    request.getParameter("newEmail"), request.getParameter("newPhone"), new Role(),
                    "");
            request.setAttribute("message", "Client updated");
        } catch (NullPointerException e) {
            request.setAttribute("error", "Wrong parameters");
        } catch (ServiceException | NameException e) {
            request.setAttribute("error", e.getMessage());
        }
    }

    /**
     * Process update client pet's name, age and sex action.
     *
     * @param request user's request.
     */
    private void updateClientPet(final HttpServletRequest request) {
        try {
            this.clinicService.updateClientPet(request.getParameter("name"),
                    request.getParameter("petName"),
                    Integer.valueOf(request.getParameter("petAge")),
                    request.getParameter("petSex").toLowerCase().equals("m") ? Sex.M : Sex.F);
            request.setAttribute("message", "Client's pet updated");
        } catch (NullPointerException e) {
            request.setAttribute("error", "Wrong parameters");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Age must be a number");
        } catch (ServiceException | NameException e) {
            request.setAttribute("error", e.getMessage());
        }
    }

    /**
     * Build error html.
     *
     * @param request user's request.
     * @return error's html.
     */
    private String viewError(final HttpServletRequest request) {
        final StringBuilder builder = new StringBuilder();
        if (request.getAttribute("error") != null) {
            builder.append("<p style='color: red;'>");
            builder.append(this.escapeHTML((String) request.getAttribute("error")));
            builder.append(".</p>");
        }
        return builder.toString();
    }

    /**
     * Build information message html.
     *
     * @param request user's request.
     * @return message's html.
     */
    private String viewMessage(final HttpServletRequest request) {
        final StringBuilder builder = new StringBuilder();
        if (request.getAttribute("message") != null) {
            builder.append("<p style='color: green;'>");
            builder.append(this.escapeHTML((String) request.getAttribute("message")));
            builder.append(".</p>");
        }
        return builder.toString();
    }

    /**
     * Build add client form.
     *
     * @param request user's request.
     * @return form's html.
     */
    private String viewAddClientForm(final HttpServletRequest request) {
        return "<p>Add client:" +
                "<form action='" +
                request.getContextPath() + request.getServletPath() + "' method='post'>" +
                "Position: <input type='text' name='position'> " +
                "Name: <input type='text' name='name'> " +
                "Email: <input type='text' name='email'> " +
                "Phone: <input type='text' name='phone'> " +
                "<input type='hidden' name ='action' value='add'>" +
                "<input type='submit' value='Submit'>" +
                "</form>";
    }

    /**
     * Build set client's pet form.
     *
     * @param request user's request.
     * @return form's html.
     */
    private String viewSetClientPetForm(final HttpServletRequest request) {
        final StringBuilder builder = new StringBuilder();
        builder.append("<p>Set client's pet:");
        builder.append("<form action='")
                .append(request.getContextPath())
                .append(request.getServletPath())
                .append("' method='post'>");
        builder.append("Name: <input type='text' name='name'> ");
        builder.append("Pet's type: <select name='petType'>");
        for (final String petType : this.clinicService.getKnownPetTypes()) {
            builder.append("<option value='").append(petType).append("'>").append(petType).append("</option>");
        }
        builder.append("</select> ");
        builder.append("Pet's name: <input type='text' name='petName'> ");
        builder.append("Pet's age: <input type='text' name='petAge'> ");
        builder.append("Pet's sex: <input type='text' name='petSex'> ");
        builder.append("<input type='hidden' name ='action' value='setPet'>");
        builder.append("<input type='submit' value='Submit'>");
        builder.append("</form>");
        return builder.toString();
    }

    /**
     * Build delete client form.
     *
     * @param request user's request.
     * @return form's html.
     */
    private String viewDelClientForm(final HttpServletRequest request) {
        return "<p>Delete client:" +
                "<form action='" + request.getContextPath() + request.getServletPath() + "' method='post'>" +
                "Name: <input type='text' name='name'> " +
                "<input type='hidden' name ='action' value='delClient'>" +
                "<input type='submit' value='Submit'>" +
                "</form>";
    }

    /**
     * Build delete client's pet form.
     *
     * @param request user's request.
     * @return form's html.
     */
    private String viewDelClientPetForm(final HttpServletRequest request) {
        return "<p>Delete client's pet:" +
                "<form action='" + request.getContextPath() + request.getServletPath() + "' method='post'>" +
                "Name: <input type='text' name='name'> " +
                "<input type='hidden' name ='action' value='delClientPet'>" +
                "<input type='submit' value='Submit'>" +
                "</form>";
    }

    /**
     * Build update client form.
     *
     * @param request user's request.
     * @return form's html.
     */
    private String viewUpdateClientForm(final HttpServletRequest request) {
        return "<p>Update client:" +
                "<form action='" + request.getContextPath() + request.getServletPath() + "' method='post'>" +
                "Name: <input type='text' name='name'> " +
                "New name: <input type='text' name='newName'> " +
                "New email: <input type='text' name='newEmail'> " +
                "New phone: <input type='text' name='newPhone'> " +
                "<input type='hidden' name ='action' value='upClient'>" +
                "<input type='submit' value='Submit'>" +
                "</form>";
    }

    /**
     * Build update client's pet name form.
     *
     * @param request user's request.
     * @return form's html.
     */
    private String viewUpdateClientPetForm(final HttpServletRequest request) {
        return "<p>Update client's pet:" +
                "<form action='" + request.getContextPath() + request.getServletPath() + "' method='post'>" +
                "Name: <input type='text' name='name'> " +
                "Pet's new name: <input type='text' name='petName'> " +
                "Pet's new age: <input type='text' name='petAge'> " +
                "Pet's new sex: <input type='text' name='petSex'> " +
                "<input type='hidden' name ='action' value='upClientPet'>" +
                "<input type='submit' value='Submit'>" +
                "</form>";
    }

    /**
     * Build all clients in clinic.
     *
     * @return html with all clients.
     */
    private String viewClients(final HttpServletRequest request) {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.viewFilters(request));

        final Client[] clients = this.getFilteredClients(request);
        if (clients.length > 0) {
            builder.append("<p>Clients:</p>");
            builder.append("<table style='border: 1px solid black;'>");
            for (int i = 0; i < clients.length; i++) {
                final Client client = clients[i];
                if (client == null) {
                    builder.append("<tr><td style='border: 1px solid black;'>").
                            append(String.format("%d. VACANT.", i + 1)).
                            append("</td></tr>");
                } else {
                    builder.append("<tr><td style='border: 1px solid black;'>").
                            append(String.format("%s.", this.escapeHTML(client.toString()))).
                            append("</td></tr>");
                }
            }
            builder.append("</table>");
        } else {
            builder.append("<p>No clients found.</p>");
        }

        return builder.toString();
    }

    /**
     * Build filter forms.
     *
     * @param request user's request.
     * @return html with filter forms.
     */
    private String viewFilters(final HttpServletRequest request) {
        final StringBuilder builder = new StringBuilder();

        builder.append("<p>Filter clients:</p>");

        builder.append("<form action='")
                .append(request.getContextPath())
                .append(request.getServletPath())
                .append("' method='get'>");
        builder.append("By name: ").append("<input type='text' name='filterName' value='");
        if (request.getParameterMap().containsKey("filterName") &&
                !request.getParameter("filterName").isEmpty()) {
            builder.append(this.escapeHTML(request.getParameter("filterName")));
        }
        builder.append("'> ");
        builder.append("<input type='submit' value='Submit'>");
        builder.append("</form>");

        builder.append("<form action='")
                .append(request.getContextPath())
                .append(request.getServletPath())
                .append("' method='get'>");
        builder.append("By pet's name: ").append("<input type='text' name='filterPetName' value='");
        if (request.getParameterMap().containsKey("filterPetName") &&
                !request.getParameter("filterPetName").isEmpty()) {
            builder.append(this.escapeHTML(request.getParameter("filterPetName")));
        }
        builder.append("'> ");
        builder.append("<input type='submit' value='Submit'>");
        builder.append("</form>");

        return builder.toString();
    }

    /**
     * Get array of clients considering user's filters.
     *
     * @param request user's request.
     * @return array of clients.
     */
    private Client[] getFilteredClients(final HttpServletRequest request) {
        Client[] clients;
        if (request.getParameterMap().containsKey("filterName")) {
            try {
                clients = new Client[]{this.clinicService.findClientByName(request.getParameter("filterName"))};
            } catch(ServiceException e) {
                clients = new Client[]{};
            }
        } else if (request.getParameterMap().containsKey("filterPetName")) {
            clients = this.clinicService.findClientsByPetName(request.getParameter("filterPetName"));
        } else {
            clients = this.clinicService.getAllClients();
        }
        return clients;
    }

    /**
     * Escape html special characters in string.
     *
     * @param string input string.
     * @return escaped string.
     */
    private String escapeHTML(final String string) {
        return string
                .replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;");
    }
}