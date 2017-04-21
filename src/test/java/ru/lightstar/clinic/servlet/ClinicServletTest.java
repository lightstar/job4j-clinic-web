package ru.lightstar.clinic.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * <code>ClinicServlet</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ClinicServletTest extends Mockito {

    /**
     * Test initializing servlet.
     */
    @Test
    public void whenDoPostThenResult() throws ServletException {
        final ClinicService clinicService = mock(ClinicService.class);
        final RoleService roleService = mock(RoleService.class);
        final ServletContext context = mock(ServletContext.class);

        when(context.getAttribute("clinicService")).thenReturn(clinicService);
        when(context.getAttribute("roleService")).thenReturn(roleService);

        final ClinicServlet clinicServlet = spy(new ClinicServlet(){});
        doReturn(context).when(clinicServlet).getServletContext();

        clinicServlet.init();

        verify(context, atLeastOnce()).getAttribute("clinicService");
        verify(context, atLeastOnce()).getAttribute("roleService");
        assertThat(clinicServlet.clinicService, is(clinicService));
    }
}