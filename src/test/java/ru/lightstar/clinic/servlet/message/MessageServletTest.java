package ru.lightstar.clinic.servlet.message;

import org.junit.Test;
import ru.lightstar.clinic.servlet.ServletTest;

import javax.servlet.ServletException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * <code>MessageServlet</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class MessageServletTest extends ServletTest {

    /**
     * Test initializing servlet.
     */
    @Test
    public void whenDoPostThenResult() throws ServletException {
        final MessageServlet messageServlet = spy(new MessageServlet(){});
        doReturn(this.context).when(messageServlet).getServletContext();

        messageServlet.init();

        verify(this.context, atLeastOnce()).getAttribute("clinicService");
        verify(this.context, atLeastOnce()).getAttribute("roleService");
        verify(this.context, atLeastOnce()).getAttribute("messageService");
        assertThat(messageServlet.messageService, is(this.messageService));
    }
}