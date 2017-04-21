package ru.lightstar.clinic.persistence.hibernate;

import javax.servlet.ServletContext;

/**
 * <code>Message</code> class.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class HibernateMessageService extends HibernateService {

    /**
     * Constructs <code>HibernateMessageService</code> object.
     *
     * @param context servlet context.
     */
    public HibernateMessageService(final ServletContext context) {
        super(context);
    }
}
