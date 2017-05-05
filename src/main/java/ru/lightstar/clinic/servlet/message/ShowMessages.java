package ru.lightstar.clinic.servlet.message;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet used to show all client's messages.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowMessages extends MessageServlet {

    /**
     * {@inheritDoc}
     */
    public ShowMessages() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    ShowMessages(final ClinicService clinicService, final RoleService roleService,
                 final MessageService messageService) {
        super(clinicService, roleService, messageService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        super.doGet(request, response);

        final Client client = this.getClientByNameParameterOrGoHome(request, response);
        if (client == Client.NONE) {
            return;
        }

        try {
            request.setAttribute("messages", this.messageService.getClientMessages(client));
        } catch (RuntimeException e) {
            request.getSession().setAttribute("error", "Unknown error.");
        }

        request.getRequestDispatcher("/WEB-INF/view/ShowMessages.jsp").forward(request, response);
    }
}
