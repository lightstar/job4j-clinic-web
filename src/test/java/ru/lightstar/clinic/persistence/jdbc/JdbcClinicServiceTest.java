package ru.lightstar.clinic.persistence.jdbc;

import org.junit.Test;
import ru.lightstar.clinic.JdbcConnectionMocker;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.PersistentClinicServiceTest;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <code>JdbcClinicService</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcClinicServiceTest extends PersistentClinicServiceTest {

    /**
     * Helper object used to mock jdbc connection.
     */
    private final JdbcConnectionMocker jdbcMocker;

    /**
     * Constructs <code>JdbcClinicServiceTest</code> object.
     */
    public JdbcClinicServiceTest() {
        super();
        final ServletContext context = mock(ServletContext.class);
        this.jdbcMocker = new JdbcConnectionMocker();
        final Connection connection = this.jdbcMocker.getConnection();
        when(context.getAttribute("jdbcConnection")).thenReturn(connection);
        this.clinicService = new JdbcClinicService(context);
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenLoadClinicThenItLoads() throws ServiceException {
        super.whenLoadClinicThenItLoads();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenAddClientThenItAdds() throws ServiceException, NameException, SQLException {
        super.whenAddClientThenItAdds();

        verify(this.jdbcMocker.getAddClientStatement(), times(1))
                .setInt(1, 7);
        verify(this.jdbcMocker.getAddClientStatement(), times(1))
                .setString(2, "Petya");
        verify(this.jdbcMocker.getAddClientStatement(), times(1))
                .setString(3, "petya@mail.ru");
        verify(this.jdbcMocker.getAddClientStatement(), times(1))
                .setString(4, "123123");
        verify(this.jdbcMocker.getAddClientStatement(), times(1))
                .executeUpdate();
        verify(this.jdbcMocker.getGeneratedKeyForAddClientResultSet(), times(1))
                .getInt(1);
    }

    /**
     * {@inheritDoc}
     */
    @Test(expected = ServiceException.class)
    @Override
    public void whenAddClientWithExceptionThenItDoNotAdds() throws ServiceException, NameException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getAddClientStatement()).executeUpdate();
        super.whenAddClientWithExceptionThenItDoNotAdds();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenSetClientPetThenItSets() throws ServiceException, NameException, SQLException {
        super.whenSetClientPetThenItSets();

        verify(this.jdbcMocker.getSetClientPetStatement(), times(1))
                .setInt(1, 4);
        verify(this.jdbcMocker.getSetClientPetStatement(), times(1))
                .setString(2, "fish");
        verify(this.jdbcMocker.getSetClientPetStatement(), times(1))
                .setString(3, "Beauty");
        verify(this.jdbcMocker.getSetClientPetStatement(), times(1))
                .setInt(4, 2);
        verify(this.jdbcMocker.getSetClientPetStatement(), times(1))
                .setString(5, "f");
        verify(this.jdbcMocker.getSetClientPetStatement(), times(1))
                .executeUpdate();
        verify(this.jdbcMocker.getGeneratedKeyForSetClientPetResultSet(), times(1))
                .getInt(1);
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenSetClientPetWithExceptionThenItDoNotSets()
            throws ServiceException, NameException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getSetClientPetStatement()).executeUpdate();
        super.whenSetClientPetWithExceptionThenItDoNotSets();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenSetClientPetWithExceptionAndClientHavingPetThenItDoNotSets()
            throws ServiceException, NameException, SQLException {
        this.addClientWithPet();
        doThrow(SQLException.class).when(this.jdbcMocker.getSetClientPetStatement()).executeUpdate();
        super.whenSetClientPetWithExceptionAndClientHavingPetThenItDoNotSets();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenUpdateClientThenItUpdates() throws NameException, ServiceException, SQLException {
        super.whenUpdateClientThenItUpdates();

        verify(this.jdbcMocker.getUpdateClientStatement(), times(1))
                .setString(1, "Vova");
        verify(this.jdbcMocker.getUpdateClientStatement(), times(1))
                .setString(2, "vova@mail.ru");
        verify(this.jdbcMocker.getUpdateClientStatement(), times(1))
                .setString(3, "456456");
        verify(this.jdbcMocker.getUpdateClientStatement(), times(1))
                .setString(4, "Petya");
        verify(this.jdbcMocker.getUpdateClientStatement(), times(1))
                .executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenUpdateClientWithExceptionThenItDoNotUpdates()
            throws NameException, ServiceException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getUpdateClientStatement()).executeUpdate();
        super.whenUpdateClientWithExceptionThenItDoNotUpdates();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenUpdateClientPetThenItUpdates() throws NameException, ServiceException, SQLException {
        super.whenUpdateClientPetThenItUpdates();

        verify(this.jdbcMocker.getUpdateClientPetStatement(), times(1))
                .setString(1, "Summer");
        verify(this.jdbcMocker.getUpdateClientPetStatement(), times(1))
                .setInt(2, 3);
        verify(this.jdbcMocker.getUpdateClientPetStatement(), times(1))
                .setString(3, "m");
        verify(this.jdbcMocker.getUpdateClientPetStatement(), times(1))
                .setInt(4, 4);
        verify(this.jdbcMocker.getUpdateClientPetStatement(), times(1))
                .executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenUpdateClientPetWithExceptionThenItDoNotUpdates()
            throws NameException, ServiceException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getUpdateClientPetStatement()).executeUpdate();
        super.whenUpdateClientPetWithExceptionThenItDoNotUpdates();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    public void whenDeleteClientPetThenItDeletes() throws NameException, ServiceException, SQLException {
        super.whenDeleteClientPetThenItDeletes();

        verify(this.jdbcMocker.getDeleteClientPetStatement(), times(1))
                .setInt(1, 4);
        verify(this.jdbcMocker.getDeleteClientPetStatement(), times(1))
                .executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenDeleteClientPetWithExceptionThenItDoNotDeletes()
            throws NameException, ServiceException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getDeleteClientPetStatement()).executeUpdate();
        super.whenDeleteClientPetWithExceptionThenItDoNotDeletes();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    public void whenDeleteClientThenItDeletes() throws NameException, ServiceException, SQLException {
        super.whenDeleteClientThenItDeletes();

        verify(this.jdbcMocker.getDeleteClientStatement(), times(1))
                .setInt(1, 4);
        verify(this.jdbcMocker.getDeleteClientStatement(), times(1))
                .executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenDeleteClientWithExceptionThenItDoNotDeletes()
            throws NameException, ServiceException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getDeleteClientStatement()).executeUpdate();
        super.whenDeleteClientWithExceptionThenItDoNotDeletes();
    }
}