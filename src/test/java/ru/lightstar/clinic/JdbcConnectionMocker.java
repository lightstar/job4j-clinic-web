package ru.lightstar.clinic;

import org.mockito.Mockito;
import ru.lightstar.clinic.jdbc.JdbcClinicService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Helper object used to get mocked jdbc connection with some test data.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcConnectionMocker extends Mockito {

    /**
     * Get mocked jdbc connection with test data.
     *
     * @return mocked jdbc connection.
     */
    public Connection getConnection() {
        final Connection connection = mock(Connection.class);

        try {
            final ResultSet emptyResultSet = mock(ResultSet.class);
            when(emptyResultSet.next()).thenReturn(false);

            final Statement statement = mock(Statement.class);
            when(statement.executeQuery(JdbcClinicService.LOAD_SQL)).thenReturn(emptyResultSet);

            when(connection.createStatement()).thenReturn(statement);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return connection;
    }
}
