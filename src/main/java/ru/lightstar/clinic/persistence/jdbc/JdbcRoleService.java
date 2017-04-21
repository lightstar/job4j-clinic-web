package ru.lightstar.clinic.persistence.jdbc;

import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Service operating on roles which uses jdbc.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcRoleService extends JdbcService implements RoleService {

    /**
     * Constructs <code>JdbcRoleService</code> object.
     *
     * @param context servlet context.
     */
    public JdbcRoleService(final ServletContext context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Role> getAllRoles() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role getRoleByName(final String name) throws ServiceException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRole(final String name) throws ServiceException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRole(final String name) throws ServiceException {

    }
}
