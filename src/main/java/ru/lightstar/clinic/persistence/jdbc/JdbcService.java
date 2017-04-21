package ru.lightstar.clinic.persistence.jdbc;

import javax.servlet.ServletContext;
import java.sql.Connection;

/**
 * Base class for service for operating on some model using jdbc.
 *
 * @author LightStar
 * @since 0.0.1
 */
public abstract class JdbcService {

    /**
     * Jdbc connection used by this service.
     */
    private final Connection connection;

    /**
     * Constructs <code>JdbcClinicService</code> object.
     */
    public JdbcService(final ServletContext context) {
        super();
        this.connection = (Connection) context.getAttribute("jdbcConnection");
    }
}
