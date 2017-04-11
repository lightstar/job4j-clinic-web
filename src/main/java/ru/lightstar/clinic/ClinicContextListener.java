package ru.lightstar.clinic;

import ru.lightstar.clinic.store.ClinicCache;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Listener executed on application initialize and destroy.
 * Servlet context attributes are set here.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ClinicContextListener implements ServletContextListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("clinicService", ClinicCache.getService());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextDestroyed(final ServletContextEvent servletContextEvent) {
    }
}
