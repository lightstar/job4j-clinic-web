package ru.lightstar.clinic.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.ClinicService;

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
        final ServletContext context = mock(ServletContext.class);

        when(context.getAttribute("clinicService")).thenReturn(clinicService);

        final ClinicServlet clinicServlet = spy(new ClinicServlet(){});
        doReturn(context).when(clinicServlet).getServletContext();

        clinicServlet.init();

        verify(context, atLeastOnce()).getAttribute("clinicService");
        assertThat(clinicServlet.clinicService, is(clinicService));
    }
}