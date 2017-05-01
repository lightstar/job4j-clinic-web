package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Message;
import ru.lightstar.clinic.persistence.MessageService;

import java.util.List;

/**
 * Service operating on messages which uses hibernate.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Service
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
     * @param sessionFactory hibernate's session factory.
     */
    @Autowired
    public HibernateMessageService(final SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Message> getClientMessages(final Client client) throws ServiceException {
        return this.doInTransaction(session -> session
                .createQuery(CLIENT_MESSAGES_HQL)
                .setParameter("client", client)
                .list());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessage(final Client client, final String text) throws ServiceException {
        if (text.isEmpty()) {
            throw new ServiceException("Text is empty");
        }

        this.doInTransaction(session -> {
            session.save(new Message(client, text));
            return null;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMessage(final Client client, final int id) throws ServiceException {
        this.doInTransaction(session -> {
            session.createQuery(DELETE_MESSAGE_HQL)
                    .setParameter("client", client)
                    .setParameter("id", id)
                    .executeUpdate();
            return null;
        });
    }
}
