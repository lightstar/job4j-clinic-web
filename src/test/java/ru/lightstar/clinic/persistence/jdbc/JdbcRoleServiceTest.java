package ru.lightstar.clinic.persistence.jdbc;

import org.junit.Test;
import ru.lightstar.clinic.JdbcConnectionMocker;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.RoleServiceTest;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <code>JdbcRoleService</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcRoleServiceTest extends RoleServiceTest {

    /**
     * Helper object used to mock jdbc connection.
     */
    private final JdbcConnectionMocker jdbcMocker;

    /**
     * Constructs <code>JdbcRoleServiceTest</code> object.
     */
    public JdbcRoleServiceTest() {
        super();

        final ServletContext context = mock(ServletContext.class);
        this.jdbcMocker = new JdbcConnectionMocker();
        final Connection connection = jdbcMocker.getConnection();
        when(context.getAttribute("jdbcConnection")).thenReturn(connection);

        this.roleService = new JdbcRoleService(context);
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenGetAllRolesThenResult() throws ServiceException {
        super.whenGetAllRolesThenResult();
    }

    /**
     * Test correctness of <code>getAllRoles</code> method when exception is thrown.
     */
    @Test(expected = ServiceException.class)
    public void whenGetAllRolesWithExceptionThenException() throws ServiceException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getAllRolesResultSet()).next();
        super.whenGetAllRolesThenResult();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenGetRoleByNameThenResult() throws ServiceException, SQLException {
        super.whenGetRoleByNameThenResult();
        verify(this.jdbcMocker.getRoleByNameStatement(), times(1))
                .setString(1, "client");
    }

    /**
     * Check correctness of <code>getRoleByName</code> method when role can't be found.
     */
    @Test(expected = ServiceException.class)
    public void whenGetRoleByNameNotFoundThenException() throws ServiceException, SQLException {
        when(this.jdbcMocker.getRoleByNameResultSet().next()).thenReturn(false);
        super.whenGetRoleByNameThenResult();
    }

    /**
     * Test correctness of <code>getRoleByName</code> method when exception is thrown.
     */
    @Test(expected = ServiceException.class)
    public void whenGetRoleByNameWithExceptionThenException() throws ServiceException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getRoleByNameResultSet()).next();
        super.whenGetRoleByNameThenResult();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenAddRoleThenItAdds() throws ServiceException, SQLException {
        when(this.jdbcMocker.getRoleByNameResultSet().next()).thenReturn(false);
        super.whenAddRoleThenItAdds();

        verify(this.jdbcMocker.getAddRoleStatement(), times(1))
                .setString(1, "manager");
        verify(this.jdbcMocker.getAddRoleStatement(), times(1))
                .executeUpdate();
    }

    /**
     * Test correctness of <code>addRole</code> when role already exists.
     */
    @Test(expected = ServiceException.class)
    public void whenAddExistingRoleThenException() throws ServiceException, SQLException {
        when(this.jdbcMocker.getRoleByNameResultSet().next()).thenReturn(true);
        super.whenAddRoleThenItAdds();
    }

    /**
     * Test correctness of <code>addRole</code> method when exception is thrown.
     */
    @Test(expected = ServiceException.class)
    public void whenAddRoleWithExceptionThenException() throws ServiceException, SQLException {
        when(this.jdbcMocker.getRoleByNameResultSet().next()).thenReturn(false);
        doThrow(SQLException.class).when(this.jdbcMocker.getAddRoleStatement()).executeUpdate();
        super.whenAddRoleThenItAdds();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenDeleteRoleThenItDeletes() throws ServiceException, SQLException {
        super.whenDeleteRoleThenItDeletes();

        verify(this.jdbcMocker.getDeleteRoleStatement(), times(1))
                .setString(1, "client");
        verify(this.jdbcMocker.getDeleteRoleStatement(), times(1))
                .executeUpdate();
    }

    /**
     * Test correctness of <code>deleteRole</code> method when role is taken by some client.
     */
    @Test(expected = ServiceException.class)
    public void whenDeleteBusyRoleThenException() throws ServiceException, SQLException {
        when(this.jdbcMocker.getRoleBusyResultSet().next()).thenReturn(true);
        super.whenDeleteRoleThenItDeletes();
    }

    /**
     * Test correctness of <code>deleteRole</code> method when exception is thrown.
     */
    @Test(expected = ServiceException.class)
    public void whenDeleteRoleWithExceptionThenException() throws ServiceException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getDeleteRoleStatement()).executeUpdate();
        super.whenDeleteRoleThenItDeletes();
    }
}