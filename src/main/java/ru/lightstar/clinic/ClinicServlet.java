package ru.lightstar.clinic;

import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.io.IteratorInput;

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
        writer.append(this.viewUpdateClientNameForm(request));
        writer.append(this.viewUpdateClientPetNameForm(request));
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
                case "setpet":
                    this.setClientPet(request);
                    break;
                case "delclient":
                    this.deleteClient(request);
                    break;
                case "delclientpet":
                    this.deleteClientPet(request);
                    break;
                case "upclientname":
                    this.updateClientName(request);
                    break;
                case "upclientpetname":
                    this.updateClientPetName(request);
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
                    request.getParameter("name"));
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
            this.clinicService.setClientPet(request.getParameter("name"), request.getParameter("pettype"),
                    request.getParameter("petname"));
            request.setAttribute("message", "Pet was set");
        } catch (NullPointerException e) {
            request.setAttribute("error", "Wrong parameters");
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
     * Process update client's name action.
     *
     * @param request user's request.
     */
    private void updateClientName(final HttpServletRequest request) {
        try {
            this.clinicService.updateClientName(request.getParameter("name"), request.getParameter("newname"));
            request.setAttribute("message", "Client's name updated");
        } catch (NullPointerException e) {
            request.setAttribute("error", "Wrong parameters");
        } catch (ServiceException | NameException e) {
            request.setAttribute("error", e.getMessage());
        }
    }

    /**
     * Process update client pet's name action.
     *
     * @param request user's request.
     */
    private void updateClientPetName(final HttpServletRequest request) {
        try {
            this.clinicService.updateClientPetName(request.getParameter("name"), request.getParameter("petname"));
            request.setAttribute("message", "Client pet's name updated");
        } catch (NullPointerException e) {
            request.setAttribute("error", "Wrong parameters");
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
                request.getContextPath() + "/' method='post'>" +
                "Name: <input type='text' name='name'> " +
                "Position: <input type='text' name='position'> " +
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
        builder.append("<form action='").append(request.getContextPath()).append("/' method='post'>");
        builder.append("Name: <input type='text' name='name'> ");
        builder.append("Pet type: <select name='pettype'>");
        for (final String petType : this.clinicService.getKnownPetTypes()) {
            builder.append("<option value='").append(petType).append("'>").append(petType).append("</option>");
        }
        builder.append("</select> ");
        builder.append("Pet name: <input type='text' name='petname'> ");
        builder.append("<input type='hidden' name ='action' value='setpet'>");
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
                "<form action='" + request.getContextPath() + "/' method='post'>" +
                "Name: <input type='text' name='name'> " +
                "<input type='hidden' name ='action' value='delclient'>" +
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
                "<form action='" + request.getContextPath() + "/' method='post'>" +
                "Name: <input type='text' name='name'> " +
                "<input type='hidden' name ='action' value='delclientpet'>" +
                "<input type='submit' value='Submit'>" +
                "</form>";
    }

    /**
     * Build update client's name form.
     *
     * @param request user's request.
     * @return form's html.
     */
    private String viewUpdateClientNameForm(final HttpServletRequest request) {
        return "<p>Update client's name:" +
                "<form action='" + request.getContextPath() + "/' method='post'>" +
                "Name: <input type='text' name='name'> " +
                "New name: <input type='text' name='newname'> " +
                "<input type='hidden' name ='action' value='upclientname'>" +
                "<input type='submit' value='Submit'>" +
                "</form>";
    }

    /**
     * Build update client pet's name form.
     *
     * @param request user's request.
     * @return form's html.
     */
    private String viewUpdateClientPetNameForm(final HttpServletRequest request) {
        return "<p>Update client pet's name:" +
                "<form action='" + request.getContextPath() + "/' method='post'>" +
                "Name: <input type='text' name='name'> " +
                "Pet's new name: <input type='text' name='petname'> " +
                "<input type='hidden' name ='action' value='upclientpetname'>" +
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

        builder.append("<form action='").append(request.getContextPath()).append("/' method='get'>");
        builder.append("By name: ").append("<input type='text' name='filter_name' value='");
        if (request.getParameterMap().containsKey("filter_name") &&
                !request.getParameter("filter_name").isEmpty()) {
            builder.append(this.escapeHTML(request.getParameter("filter_name")));
        }
        builder.append("'> ");
        builder.append("<input type='submit' value='Submit'>");
        builder.append("</form>");

        builder.append("<form action='").append(request.getContextPath()).append("/' method='get'>");
        builder.append("By pet's name: ").append("<input type='text' name='filter_petname' value='");
        if (request.getParameterMap().containsKey("filter_petname") &&
                !request.getParameter("filter_petname").isEmpty()) {
            builder.append(this.escapeHTML(request.getParameter("filter_petname")));
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
        if (request.getParameterMap().containsKey("filter_name")) {
            try {
                clients = new Client[]{this.clinicService.findClientByName(request.getParameter("filter_name"))};
            } catch(ServiceException e) {
                clients = new Client[]{};
            }
        } else if (request.getParameterMap().containsKey("filter_petname")) {
            clients = this.clinicService.findClientsByPetName(request.getParameter("filter_petname"));
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