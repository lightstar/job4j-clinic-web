package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.persistence.RoleService;

import java.util.List;

/**
 * Service operating on roles which uses hibernate.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Service
public class HibernateRoleService implements RoleService {

    /**
     * Spring's hibernate template.
     */
    private final HibernateTemplate hibernateTemplate;

    /**
     * HQL used to get all roles from database.
     */
    public static final String ALL_ROLES_HQL = "from Role order by id";

    /**
     * HQL used to get role by name from database.
     */
    public static final String ROLE_BY_NAME_HQL = "from Role where name = :name";

    /**
     * HQL used to delete role from database.
     */
    public static final String DELETE_ROLE_HQL = "delete from Role where name = :name";

    /**
     * HQL used to get client by role's name from database.
     */
    public static final String CLIENT_BY_ROLE_HQL = "from Client where role.name = :name";

    /**
     * Constructs <code>HibernateRoleService</code> object.
     *
     * @param hibernateTemplate spring's hibernate template.
     */
    @Autowired
    public HibernateRoleService(final HibernateTemplate hibernateTemplate) {
        super();
        this.hibernateTemplate = hibernateTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Role> getAllRoles() {
        return (List<Role>) this.hibernateTemplate.find(ALL_ROLES_HQL);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Role getRoleByName(final String name) throws ServiceException {
        final List<Role> roles = (List<Role>) this.hibernateTemplate.findByNamedParam(ROLE_BY_NAME_HQL,
                "name", name);
        if (roles.size() == 0) {
            throw new ServiceException("Role doesn't exists");
        }
        return roles.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void addRole(final String name) throws ServiceException {
        if (name.isEmpty()) {
            throw new ServiceException("Name is empty");
        }

        if (this.hibernateTemplate.findByNamedParam(ROLE_BY_NAME_HQL, "name", name).size() > 0) {
            throw new ServiceException("Role already exists");
        }

        this.hibernateTemplate.save(new Role(name));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteRole(final String name) throws ServiceException {
        if (this.isRoleBusy(name)) {
            throw new ServiceException("Some client has this role");
        }

        this.hibernateTemplate.executeWithNativeSession((final Session session) -> {
            session.createQuery(DELETE_ROLE_HQL)
                    .setParameter("name", name)
                    .executeUpdate();
            return null;
        });
    }

    /**
     * Check if role is busy with some client.
     *
     * @param name role's name.
     * @return <code>true</code> if some client has this role and <code>false</code> - otherwise.
     */
    private boolean isRoleBusy(final String name) {
        return this.hibernateTemplate.findByNamedParam(CLIENT_BY_ROLE_HQL, "name", name).size() > 0;
    }
}
