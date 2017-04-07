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

    private static final int CLINIC_SIZE = 10;

    private final Clinic clinic;

    private final ClinicService clinicService;

    public ClinicServlet() {
        this.clinic = new Clinic(CLINIC_SIZE);
        this.clinicService = new ClinicService(new IteratorInput(), new DummyOutput(), clinic);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter writer = response.getWriter();
        writer.append(
                "<!DOCTYPE html>" +
                "<html>" +
                    "<head>" +
                        "<title>Pet's clinic</title>" +
                    "</head>" +
                    "<body>" +
                        "<form action='" + request.getContextPath() + "/' method='post'>" +
                            "Name: <input type='text' name='name'>" +
                            "Position: <input type='text' name='position'>" +
                            "<input type='submit' value='Submit'>" +
                        "</form>" +
                        this.viewClients() +
                    "</body>" +
                "</html>"
        );
        writer.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            this.clinicService.addClient(Integer.valueOf(request.getParameter("position")) - 1,
                    request.getParameter("name"));
        } catch (ServiceException | NameException e) {
            e.printStackTrace();
        }
        this.doGet(request, response);
    }

    private String viewClients() {
        final Client[] clients = this.clinicService.getAllClients();

        final StringBuilder builder = new StringBuilder();
        builder.append("<p>Clients:</p>");
        builder.append("<table style='border: 1px solid black;'>");
        for (final Client client : clients) {
            if (client == null) {
                continue;
            }
            builder.append("<tr><td style='border: 1px solid black;'>").
                    append(client).
                    append("</td></tr>");
        }
        builder.append("</table>");
        return builder.toString();
    }
}
