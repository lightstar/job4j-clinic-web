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
 * Servlet used to delete client's message.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class DeleteMessage extends MessageServlet {

    /**
     * {@inheritDoc}
     */
    public DeleteMessage() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    DeleteMessage(final ClinicService clinicService, final RoleService roleService,
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
            this.messageService.deleteMessage(client, Integer.valueOf(request.getParameter("id")));
        } catch (NullPointerException | NumberFormatException e) {
            errorString = "Invalid request parameters";
        } catch (ServiceException e) {
            errorString = e.getMessage();
        }

        final String encodedName = URLEncoder.encode(client.getName(), "utf-8");
        this.finishUpdate(request, response, "Message deleted", errorString,
                "/client/message?name=" + encodedName);
    }

    /**
     * Check that needed parameters are not null.
     *
     * @param request user's request.
     */
    private void checkParameters(final HttpServletRequest request) {
        if (request.getParameter("id") == null) {
            throw new NullPointerException();
        }
    }
}
