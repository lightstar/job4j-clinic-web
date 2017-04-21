package ru.lightstar.clinic.persistence.hibernate;

import ru.lightstar.clinic.persistence.MessageService;

import javax.servlet.ServletContext;

/**
 * Service operating on messages which uses hibernate.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class HibernateMessageService extends HibernateService implements MessageService {

    /**
     * Constructs <code>HibernateMessageService</code> object.
     *
     * @param context servlet context.
     */
    public HibernateMessageService(final ServletContext context) {
        super(context);
    }
}
