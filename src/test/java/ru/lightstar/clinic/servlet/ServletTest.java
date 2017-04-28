package ru.lightstar.clinic.servlet;

import org.mockito.Mockito;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Base class for servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public abstract class ServletTest extends Mockito {

    /**
     * Mocked servlet context object used in tests.
     */
    protected final ServletContext context;

    /**
     * Mocked request object used in tests.
     */
    protected final HttpServletRequest request;

    /**
     * Mocked response object used in tests.
     */
    protected final HttpServletResponse response;

    /**
     * Mocked dispatcher object used in tests.
     */
    protected final RequestDispatcher dispatcher;

    /**
     * Mocked session object used in tests.
     */
    protected final HttpSession session;

    /**
     * Mocked clinic service used in tests.
     */
    protected final ClinicService clinicService;

    /**
     * Mocked drug service used in tests.
     */
    protected final DrugService drugService;

    /**
     * Mocked role service used in tests.
     */
    protected final RoleService roleService;

    /**
     * Mocked message service used in tests.
     */
    protected final MessageService messageService;

    /**
     * Constructs <code>ServletTest</code> object.
     */
    public ServletTest() {
        this.context = mock(ServletContext.class);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.dispatcher = mock(RequestDispatcher.class);
        this.session = mock(HttpSession.class);
        this.clinicService = mock(ClinicService.class);
        this.drugService = mock(DrugService.class);
        this.roleService = mock(RoleService.class);
        this.messageService = mock(MessageService.class);

        when(this.context.getAttribute("clinicService")).thenReturn(this.clinicService);
        when(this.context.getAttribute("drugService")).thenReturn(this.drugService);
        when(this.context.getAttribute("roleService")).thenReturn(this.roleService);
        when(this.context.getAttribute("messageService")).thenReturn(this.messageService);

        when(this.request.getContextPath()).thenReturn("/context");
        when(this.request.getSession()).thenReturn(this.session);
    }
}
