package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
public class HibernateRoleService extends HibernateService implements RoleService {

    /**
     * HQL used to get all roles from database.
     */
    public static final String ALL_ROLES_HQL = "from Role";

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
    public static final String CLIENT_BY_ROLE_HQL = "select c from Client c inner join Role r where r.name = :name";

    /**
     * Constructs <code>HibernateRoleService</code> object.
     *
     * @param sessionFactory hibernate's session factory.
     */
    @Autowired
    public HibernateRoleService(final SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Role> getAllRoles() throws ServiceException {
        return this.doInTransaction(session -> session.createQuery(ALL_ROLES_HQL).list());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role getRoleByName(final String name) throws ServiceException {
        return this.doInTransaction(session -> {
            final Role role = this.getRoleByName(session, name);
            if (role == null) {
                throw new ServiceException("Role doesn't exists");
            }
            return role;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRole(final String name) throws ServiceException {
        if (name.isEmpty()) {
            throw new ServiceException("Name is empty");
        }

        this.doInTransaction(session -> {
            if (this.getRoleByName(session, name) != null) {
                throw new ServiceException("Role already exists");
            }
            session.save(new Role(name));
            return null;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRole(final String name) throws ServiceException {
        this.doInTransaction(session -> {
            if (this.isRoleBusy(session, name)) {
                throw new ServiceException("Some client has this role");
            }

            session.createQuery(DELETE_ROLE_HQL)
                    .setParameter("name", name)
                    .executeUpdate();
            return null;
        });
    }

    /**
     * Get role by name with already opened session and in already created transaction.
     *
     * @param session already opened session.
     * @param name role's name.
     * @return role object.
     */
    private Role getRoleByName(final Session session, final String name) {
        return (Role) session.createQuery(ROLE_BY_NAME_HQL)
                .setParameter("name", name).uniqueResult();
    }

    /**
     * Check if role is busy with some client. Session must be already opened.
     *
     * @param session already opened session.
     * @param name role's name.
     * @return <code>true</code> if some client has this role and <code>false</code> - otherwise.
     */
    private boolean isRoleBusy(final Session session, final String name) {
        return session.createQuery(CLIENT_BY_ROLE_HQL)
                .setParameter("name", name).setMaxResults(1).uniqueResult() != null;
    }
}
