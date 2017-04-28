package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.Session;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Message;
import ru.lightstar.clinic.persistence.MessageService;

import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;
import java.util.List;

/**
 * Service operating on messages which uses hibernate.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class HibernateMessageService extends HibernateService implements MessageService {

    /**
     * HQL used to get client messages from database.
     */
    public static final String CLIENT_MESSAGES_HQL = "from Message where client = :client";

    /**
     * HQL used to delete client message from database.
     */
    public static final String DELETE_MESSAGE_HQL = "delete from Message where client = :client and id = :id";

    /**
     * Constructs <code>HibernateMessageService</code> object.
     *
     * @param context servlet context.
     */
    public HibernateMessageService(final ServletContext context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Message> getClientMessages(final Client client) throws ServiceException {
        try (final Session session = this.sessionFactory.openSession()) {
            return session.createQuery(CLIENT_MESSAGES_HQL)
                    .setParameter("client", client)
                    .list();
        } catch (PersistenceException e) {
            throw new ServiceException(String.format("Can't get data from database: %s", e.getMessage()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessage(final Client client, final String text) throws ServiceException {
        if (text.isEmpty()) {
            throw new ServiceException("Text is empty");
        }

        try (final Session session = this.sessionFactory.openSession()) {
            session.beginTransaction();
            final Message message = new Message(client, text);
            session.save(message);
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new ServiceException(String.format("Can't insert message into database: %s", e.getMessage()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMessage(final Client client, final int id) throws ServiceException {
        try (final Session session = this.sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery(DELETE_MESSAGE_HQL)
                    .setParameter("client", client)
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new ServiceException(String.format("Can't delete message from database: %s", e.getMessage()));
        }
    }
}
