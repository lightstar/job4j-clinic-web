package ru.lightstar.clinic.persistence.jdbc;

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
    protected final Connection connection;

    /**
     * Constructs <code>JdbcClinicService</code> object.
     *
     * @param connection jdbc connection.
     */
    public JdbcService(final Connection connection) {
        super();
        this.connection = connection;
    }
}
