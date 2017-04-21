package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.Session;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.List;

/**
 * Service operating on roles which uses hibernate.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class HibernateRoleService extends HibernateService implements RoleService {

    /**
     * Constructs <code>HibernateRoleService</code> object.
     *
     * @param context servlet context.
     */
    public HibernateRoleService(final ServletContext context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Role> getAllRoles() {
        if (this.sessionFactory == null) {
            return Collections.emptyList();
        }

        try (final Session session = this.sessionFactory.openSession()) {
            return session.createQuery("from Role").list();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role getRoleByName(final String name) throws ServiceException {
        if (this.sessionFactory == null) {
            throw new ServiceException("Role doesn't exists");
        }

        try (final Session session = this.sessionFactory.openSession()) {
            final Role role = (Role) session.createQuery("from Role where name = :name")
                    .setParameter("name", name)
                    .uniqueResult();
            if (role == null) {
                throw new ServiceException("Role doesn't exists");
            }
            return role;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRole(final String name) throws ServiceException {
        if (this.sessionFactory == null) {
            return;
        }

        if (name.isEmpty()) {
            throw new ServiceException("Name is empty");
        }

        ServiceException expectedException = null;
        try {
            this.getRoleByName(name);
        } catch (ServiceException e) {
            expectedException = e;
        }

        if (expectedException == null) {
            throw new ServiceException("Role already exists");
        }

        try (final Session session = this.sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(new Role(name));
            session.getTransaction().commit();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRole(final String name) throws ServiceException {
        if (this.sessionFactory == null) {
            return;
        }

        if (this.isRoleBusy(name)) {
            throw new ServiceException("Some client has this role");
        }

        try (final Session session = this.sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("delete from Role where name = :name")
                    .setParameter("name", name)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }

    /**
     * Check if some client has given role.
     *
     * @param name role's name.
     * @return <code>true</code> if some client has given role and <code>false</code> otherwise.
     */
    private boolean isRoleBusy(final String name) {
        if (this.sessionFactory == null) {
            return false;
        }

        try (final Session session = this.sessionFactory.openSession()) {
            return session.createQuery("from Client where role.name = :name")
                    .setParameter("name", name).setMaxResults(1).uniqueResult() != null;
        }
    }
}
