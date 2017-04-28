package ru.lightstar.clinic.servlet.message;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Servlet used to add client's message.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AddMessage extends MessageServlet {

    /**
     * {@inheritDoc}
     */
    public AddMessage() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    AddMessage(final ClinicService clinicService, final RoleService roleService,
                  final MessageService messageService) {
        super(clinicService, roleService, messageService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        final Client client = this.getClientByNameParameterOrGoHome(request, response);
        if (client == Client.NONE) {
            return;
        }

        String errorString = "";
        try {
            this.checkParameters(request);
            this.messageService.addMessage(client, request.getParameter("text")
                    .replaceAll("/r?/n","<br>"));
        } catch (NullPointerException e) {
            errorString = "Invalid request parameters";
        } catch (ServiceException e) {
            errorString = e.getMessage();
        }

        final String encodedName = URLEncoder.encode(client.getName(), "utf-8");
        this.finishUpdate(request, response, "Message added", errorString,
                    "/client/message?name=" + encodedName);
    }

    /**
     * Check that needed parameters are not null.
     *
     * @param request user's request.
     */
    private void checkParameters(final HttpServletRequest request) {
        if (request.getParameter("text") == null) {
            throw new NullPointerException();
        }
    }
}
