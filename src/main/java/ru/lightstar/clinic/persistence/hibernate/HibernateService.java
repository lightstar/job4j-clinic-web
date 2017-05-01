package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.lightstar.clinic.exception.ServiceException;

import javax.persistence.PersistenceException;

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
     *
     * @param sessionFactory hibernate's session factory.
     */
    public HibernateService(final SessionFactory sessionFactory) {
        super();
        this.sessionFactory = sessionFactory;
    }

    /**
     * Do some operation in transaction with opened session.
     *
     * @param command processed operation.
     * @throws ServiceException throws on database error.
     */
    protected <T> T doInTransaction(final Command<T>command) throws ServiceException {
        final T result;

        try (final Session session = this.sessionFactory.openSession()) {
            session.beginTransaction();
            result = command.process(session);
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new ServiceException(String.format("Database error: %s", e.getMessage()));
        }

        return result;
    }

    /**
     * Interface for some operation in context of hibernate session.
     *
     * @param <T> type of operation's return value.
     */
    @FunctionalInterface
    protected interface Command<T> {
        T process(Session session) throws ServiceException;
    }
}
