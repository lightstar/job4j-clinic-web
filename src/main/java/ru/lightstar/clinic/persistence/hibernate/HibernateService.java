package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.SessionFactory;

import javax.servlet.ServletContext;

/**
 * Base class for service for operating on some model using hibernate.
 *
 * @author LightStar
 * @since 0.0.1
 */
public abstract class HibernateService {

    /**
     * Hibernate's session factory.
     */
    protected final SessionFactory sessionFactory;

    /**
     * Constructs <code>HibernateService</code> object.
     */
    public HibernateService(final ServletContext context) {
        super();
        this.sessionFactory = (SessionFactory) context.getAttribute("sessionFactory");
    }
}
