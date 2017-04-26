package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.SessionFactory;
import org.junit.Test;
import ru.lightstar.clinic.SessionFactoryMocker;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.persistence.RoleServiceTest;

import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;
import java.sql.SQLException;

/**
 * <code>HibernateRoleService</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class HibernateRoleServiceTest extends RoleServiceTest {

    /**
     * Helper object used to mock hibernate session factory.
     */
    private final SessionFactoryMocker hibernateMocker;

    /**
     * Constructs <code>HibernateRoleServiceTest</code> object.
     */
    public HibernateRoleServiceTest() {
        super();
        final ServletContext context = mock(ServletContext.class);

        this.hibernateMocker = new SessionFactoryMocker();
        final SessionFactory sessionFactory = this.hibernateMocker.getSessionFactory();
        when(context.getAttribute("sessionFactory")).thenReturn(sessionFactory);

        this.roleService = new HibernateRoleService(context);
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
    public void whenGetAllRolesWithExceptionThenException() throws ServiceException {
        doThrow(PersistenceException.class).when(this.hibernateMocker.getAllRolesQuery()).list();
        super.whenGetAllRolesThenResult();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenGetRoleByNameThenResult() throws ServiceException, SQLException {
        super.whenGetRoleByNameThenResult();
    }

    /**
     * Check correctness of <code>getRoleByName</code> method when role can't be found.
     */
    @Test(expected = ServiceException.class)
    public void whenGetRoleByNameNotFoundThenException() throws ServiceException, SQLException {
        when(this.hibernateMocker.getRoleByNameClientQuery().uniqueResult()).thenReturn(null);
        super.whenGetRoleByNameThenResult();
    }

    /**
     * Check correctness of <code>getRoleByName</code> method when exception is thrown.
     */
    @Test(expected = ServiceException.class)
    public void whenGetRoleByNameWithExceptionThenException() throws ServiceException, SQLException {
        doThrow(PersistenceException.class).when(this.hibernateMocker.getRoleByNameClientQuery()).uniqueResult();
        super.whenGetRoleByNameThenResult();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenAddRoleThenItAdds() throws ServiceException, SQLException {
        when(this.hibernateMocker.getRoleByNameManagerQuery().uniqueResult()).thenReturn(null);
        super.whenAddRoleThenItAdds();

        verify(this.hibernateMocker.getSession(), atLeastOnce())
                .beginTransaction();
        verify(this.hibernateMocker.getSession(), times(1))
                .save(any(Role.class));
        verify(this.hibernateMocker.getTransaction(), atLeastOnce())
                .commit();
    }

    /**
     * Test correctness of <code>addRole</code> method when role already exists.
     */
    @Test(expected = ServiceException.class)
    public void whenAddExistingRoleThenException() throws ServiceException, SQLException {
        super.whenAddRoleThenItAdds();
    }

    /**
     * Test correctness of <code>addRole</code> method when exception is thrown.
     */
    @Test(expected = ServiceException.class)
    public void whenAddRoleWithExceptionThenException() throws ServiceException, SQLException {
        when(this.hibernateMocker.getRoleByNameManagerQuery().uniqueResult()).thenReturn(null);
        doThrow(PersistenceException.class).when(this.hibernateMocker.getSession()).save(any(Role.class));
        super.whenAddRoleThenItAdds();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenDeleteRoleThenItDeletes() throws ServiceException, SQLException {
        when(this.hibernateMocker.getIsRoleBusyQuery().uniqueResult()).thenReturn(null);
        super.whenDeleteRoleThenItDeletes();
    }

    /**
     * Test correctness of <code>deleteRole</code> method when role is taken by some client.
     */
    @Test(expected = ServiceException.class)
    public void whenDeleteBusyRoleThenException() throws ServiceException, SQLException {
        super.whenDeleteRoleThenItDeletes();
    }

    /**
     * Test correctness of <code>deleteRole</code> method when exception is thrown.
     */
    @Test(expected = ServiceException.class)
    public void whenDeleteRoleWithExceptionThenException() throws ServiceException, SQLException {
        when(this.hibernateMocker.getIsRoleBusyQuery().uniqueResult()).thenReturn(null);
        doThrow(PersistenceException.class).when(this.hibernateMocker.getDeleteRoleQuery()).executeUpdate();
        super.whenDeleteRoleThenItDeletes();
    }
}