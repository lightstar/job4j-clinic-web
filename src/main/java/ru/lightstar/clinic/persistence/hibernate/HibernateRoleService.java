package ru.lightstar.clinic.persistence.hibernate;

import javax.servlet.ServletContext;

/**
 * Service operating on roles.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class HibernateRoleService extends HibernateService {

    /**
     * Constructs <code>HibernateRoleService</code> object.
     *
     * @param context servlet context.
     */
    public HibernateRoleService(final ServletContext context) {
        super(context);
    }
}
