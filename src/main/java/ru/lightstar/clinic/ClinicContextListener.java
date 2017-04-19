package ru.lightstar.clinic;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.lightstar.clinic.persistence.hibernate.HibernateClinicService;
import ru.lightstar.clinic.persistence.hibernate.HibernateDrugService;
import ru.lightstar.clinic.persistence.jdbc.JdbcClinicService;
import ru.lightstar.clinic.persistence.jdbc.JdbcDrugService;
import ru.lightstar.clinic.persistence.jdbc.JdbcSettings;

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
        final String useHibernate = servletContext.getInitParameter("useHibernate");

        if (useHibernate != null &&
                (useHibernate.toLowerCase().equals("yes") || useHibernate.toLowerCase().equals("true"))) {
            this.initHibernate(servletContext);
        } else {
            this.initJdbc(servletContext);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextDestroyed(final ServletContextEvent servletContextEvent) {
        final ServletContext servletContext = servletContextEvent.getServletContext();
        this.destroyJdbc(servletContext);
        this.destroyHibernate(servletContext);
    }

    /**
     * Init hibernate-variant services.
     *
     * @param servletContext servlet context.
     */
    private void initHibernate(final ServletContext servletContext) {
        final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        servletContext.setAttribute("sessionFactory", sessionFactory);

        final HibernateClinicService clinicService = new HibernateClinicService(servletContext);
        clinicService.loadClinic();
        servletContext.setAttribute("clinicService", clinicService);

        final HibernateDrugService drugService = new HibernateDrugService(clinicService.getClinic(),
                servletContext);
        drugService.loadDrugs();
        servletContext.setAttribute("drugService", drugService);
    }

    /**
     * Init jdbc connection and jdbc-variant services.
     *
     * @param servletContext servlet context.
     */
    private void initJdbc(final ServletContext servletContext) {
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
     * Close hibernate session factory if it is exists.
     *
     * @param servletContext servlet context.
     */
    private void destroyHibernate(final ServletContext servletContext) {
        final SessionFactory sessionFactory = (SessionFactory) servletContext.getAttribute("sessionFactory");
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    /**
     * Close jdbc connection if it is exists and deregister jdbc driver
     *
     * @param servletContext servlet context.
     */
    private void destroyJdbc(final ServletContext servletContext) {
        final Connection connection = (Connection) servletContext.getAttribute("jdbcConnection");
        if (connection != null) {
            try {
                connection.close();
                DriverManager.deregisterDriver(DriverManager.getDriver(this.settings.value("jdbc.url")));
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
