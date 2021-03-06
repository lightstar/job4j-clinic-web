package ru.lightstar.clinic.servlet.drug;

import org.junit.Test;
import ru.lightstar.clinic.servlet.ServletTest;

import javax.servlet.ServletException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * <code>DrugServlet</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class DrugServletTest extends ServletTest {

    /**
     * Test initializing servlet.
     */
    @Test
    public void whenDoPostThenResult() throws ServletException {
        final DrugServlet drugServlet = spy(new DrugServlet(){});
        doReturn(this.context).when(drugServlet).getServletContext();

        drugServlet.init();

        verify(this.context, atLeastOnce()).getAttribute("clinicService");
        verify(this.context, atLeastOnce()).getAttribute("roleService");
        verify(this.context, atLeastOnce()).getAttribute("drugService");
        assertThat(drugServlet.drugService, is(this.drugService));
    }
}
