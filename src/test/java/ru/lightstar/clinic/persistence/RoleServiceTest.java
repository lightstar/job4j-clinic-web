package ru.lightstar.clinic.persistence;

import org.mockito.Mockito;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Role;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Base class for testing <code>RoleService</code> implementations.
 *
 * @author LightStar
 * @since 0.0.1
 */
public abstract class RoleServiceTest extends Mockito {

    /**
     * <code>RoleService</code> object used in all tests. Must be set in implementations.
     */
    protected RoleService roleService;

    /**
     * Test correctness of <code>getAllRoles</code> method.
     */
    public void whenGetAllRolesThenResult() throws ServiceException {
        final List<Role> roles = this.roleService.getAllRoles();

        assertThat(roles.size(), is(2));
        assertThat(roles.get(0).getId(), is(1));
        assertThat(roles.get(0).getName(), is("admin"));
        assertThat(roles.get(1).getId(), is(2));
        assertThat(roles.get(1).getName(), is("client"));
    }

    /**
     * Test correctness of <code>getRoleByName</code> method.
     */
    public void whenGetRoleByNameThenResult() throws ServiceException, SQLException {
        final Role role = this.roleService.getRoleByName("client");

        assertThat(role.getId(), is(2));
        assertThat(role.getName(), is("client"));
    }

    /**
     * Test correctness of <code>addRole</code> method.
     */
    public void whenAddRoleThenItAdds() throws ServiceException, SQLException {
        this.roleService.addRole("manager");
    }

    /**
     * Test correctness of <code>deleteRole</code> method.
     */
    public void whenDeleteRoleThenItDeletes() throws ServiceException, SQLException {
        this.roleService.deleteRole("client");
    }
}
