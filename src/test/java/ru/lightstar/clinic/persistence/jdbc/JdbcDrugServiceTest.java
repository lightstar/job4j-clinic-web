package ru.lightstar.clinic.persistence.jdbc;

import org.junit.Test;
import ru.lightstar.clinic.Clinic;
import ru.lightstar.clinic.JdbcConnectionMocker;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.PersistentDrugServiceTest;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <code>JdbcDrugService</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcDrugServiceTest extends PersistentDrugServiceTest {

    /**
     * Helper object used to mock jdbc connection.
     */
    private final JdbcConnectionMocker jdbcMocker;

    /**
     * Constructs <code>JdbcDrugServiceTest</code> object.
     */
    public JdbcDrugServiceTest() {
        super();
        this.jdbcMocker = new JdbcConnectionMocker();
        final Connection connection = jdbcMocker.getConnection();
        this.drugService = new JdbcDrugService(new Clinic(CLINIC_SIZE), connection);
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenLoadDrugsThenItLoads() throws ServiceException {
        super.whenLoadDrugsThenItLoads();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenAddDrugThenItAdds() throws ServiceException, SQLException {
        super.whenAddDrugThenItAdds();

        verify(this.jdbcMocker.getAddDrugStatement(), times(1))
                .setString(1, "aspirin");
        verify(this.jdbcMocker.getAddDrugStatement(), times(1))
                .setInt(2, 2);
        verify(this.jdbcMocker.getAddDrugStatement(), times(1))
                .executeUpdate();
        verify(this.jdbcMocker.getGeneratedKeyForAddDrugResultSet(), times(1))
                .getInt(1);
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenAddDrugWithExceptionThenItDoNotAdds() throws ServiceException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getAddDrugStatement()).executeUpdate();
        super.whenAddDrugWithExceptionThenItDoNotAdds();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenTakeDrugThenItDeletes() throws ServiceException, SQLException {
        super.whenTakeDrugThenItDeletes();

        verify(this.jdbcMocker.getDeleteDrugStatement(), times(1))
                .setInt(1, 6);
        verify(this.jdbcMocker.getDeleteDrugStatement(), times(1))
                .executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenTakeDrugWithExceptionThenItDoNotDeletes() throws ServiceException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getDeleteDrugStatement()).executeUpdate();
        super.whenTakeDrugWithExceptionThenItDoNotDeletes();
    }
}