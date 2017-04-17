package ru.lightstar.clinic.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * <code>DrugServlet</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class DrugServletTest extends Mockito {

    /**
     * Test initializing servlet.
     */
    @Test
    public void whenDoPostThenResult() throws ServletException {
        final ClinicService clinicService = mock(ClinicService.class);
        final DrugService drugService = mock(DrugService.class);
        final ServletContext context = mock(ServletContext.class);

        when(context.getAttribute("clinicService")).thenReturn(clinicService);
        when(context.getAttribute("drugService")).thenReturn(drugService);

        final DrugServlet drugServlet = spy(new DrugServlet(){});
        doReturn(context).when(drugServlet).getServletContext();

        drugServlet.init();

        verify(context, atLeastOnce()).getAttribute("clinicService");
        verify(context, atLeastOnce()).getAttribute("drugService");
        assertThat(drugServlet.clinicService, is(clinicService));
        assertThat(drugServlet.drugService, is(drugService));
    }
}
