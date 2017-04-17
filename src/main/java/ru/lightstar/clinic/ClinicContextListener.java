package ru.lightstar.clinic;

import ru.lightstar.clinic.jdbc.JdbcClinicService;
import ru.lightstar.clinic.jdbc.JdbcDrugService;
import ru.lightstar.clinic.jdbc.JdbcSettings;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Listener executed on application initialize and destroy.
 * Servlet context attributes are set here.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ClinicContextListener implements ServletContextListener {

    /**
     * <code>JdbcSettings</code> object used to fetch jdbc properties from external file.
     */
    private final JdbcSettings settings = new JdbcSettings();

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        final ServletContext servletContext = servletContextEvent.getServletContext();

        try {
            Class.forName(this.settings.value("jdbc.driver_class")).newInstance();
            final Connection connection = DriverManager.getConnection(this.settings.value("jdbc.url"),
                    this.settings.value("jdbc.username"), this.settings.value("jdbc.password"));
            servletContext.setAttribute("jdbcConnection", connection);

            final JdbcClinicService clinicService = new JdbcClinicService(servletContext);
            clinicService.loadClinic();
            servletContext.setAttribute("clinicService", clinicService);

            final JdbcDrugService drugService = new JdbcDrugService(clinicService.getClinic(), servletContext);
            drugService.loadDrugs();
            servletContext.setAttribute("drugService", drugService);
        } catch (SQLException | ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextDestroyed(final ServletContextEvent servletContextEvent) {
        final ServletContext servletContext = servletContextEvent.getServletContext();

        final Connection connection = (Connection) servletContext.getAttribute("jdbcConnection");
        try {
            connection.close();
            DriverManager.deregisterDriver(DriverManager.getDriver(this.settings.value("jdbc.url")));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
