package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class HibernateMessageService implements MessageService {

    /**
     * Spring's hibernate template.
     */
    private final HibernateTemplate hibernateTemplate;

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
     * @param hibernateTemplate spring's hibernate template.
     */
    @Autowired
    public HibernateMessageService(final HibernateTemplate hibernateTemplate) {
        super();
        this.hibernateTemplate = hibernateTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Message> getClientMessages(final Client client) {
        return (List<Message>) this.hibernateTemplate.findByNamedParam(CLIENT_MESSAGES_HQL,
                "client", client);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void addMessage(final Client client, final String text) throws ServiceException {
        if (text.isEmpty()) {
            throw new ServiceException("Text is empty");
        }

        this.hibernateTemplate.save(new Message(client, text));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteMessage(final Client client, final int id) {
        this.hibernateTemplate.executeWithNativeSession((final Session session) -> {
            session.createQuery(DELETE_MESSAGE_HQL)
                    .setParameter("client", client)
                    .setParameter("id", id)
                    .executeUpdate();
            return null;
        });
    }
}
