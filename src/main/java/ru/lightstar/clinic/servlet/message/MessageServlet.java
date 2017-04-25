package ru.lightstar.clinic.servlet.message;

import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;
import ru.lightstar.clinic.servlet.ClinicServlet;

import javax.servlet.ServletException;

/**
 * Base class for all message servlets.
 *
 * @author LightStar
 * @since 0.0.1
 */
public abstract class MessageServlet extends ClinicServlet {

    /**
     * Global message service used by all servlets. Usually it is set in {@link #init} method but in tests it is
     * provided in constructor.
     */
    protected MessageService messageService = null;

    /**
     * Constructs <code>MessageServlet</code> object.
     */
    public MessageServlet() {
        super();
    }

    /**
     * Constructs <code>MessageServlet</code> object using pre-defined clinic, role and message services
     * (used in tests).
     *
     * @param clinicService pre-defined clinic service.
     * @param roleService pre-defined role service.
     * @param messageService pre-defined message service.
     */
    MessageServlet(final ClinicService clinicService, final RoleService roleService,
                final MessageService messageService) {
        super(clinicService, roleService);
        this.messageService = messageService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.messageService = (MessageService) this.getServletContext().getAttribute("messageService");
    }
}
