package ru.lightstar.clinic;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.store.ClinicCache;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * <code>ClinicContextListener</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ClinicContextListenerTest extends Mockito {

    /**
     * Tests correct setting of <code>clinicService</code> attribute in <code>contextInitialized</code> method.
     */
    @Test
    public void whenContextInitializedThenClinicServiceIsSet() {
        final ServletContextEvent event = mock(ServletContextEvent.class);
        final ServletContext context = mock(ServletContext.class);

        when(event.getServletContext()).thenReturn(context);

        new ClinicContextListener().contextInitialized(event);

        verify(context, atLeastOnce()).setAttribute("clinicService", ClinicCache.getService());
    }
}