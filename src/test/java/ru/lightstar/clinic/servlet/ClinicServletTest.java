package ru.lightstar.clinic.servlet;

import org.junit.Test;

import javax.servlet.ServletException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * <code>ClinicServlet</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ClinicServletTest extends ServletTest {

    /**
     * Test initializing servlet.
     */
    @Test
    public void whenDoPostThenResult() throws ServletException {
        final ClinicServlet clinicServlet = spy(new ClinicServlet(){});
        doReturn(this.context).when(clinicServlet).getServletContext();

        clinicServlet.init();

        verify(this.context, atLeastOnce()).getAttribute("clinicService");
        verify(this.context, atLeastOnce()).getAttribute("roleService");
        assertThat(clinicServlet.clinicService, is(this.clinicService));
    }
}