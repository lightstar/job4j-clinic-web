package ru.lightstar.clinic.persistence.jdbc;

import ru.lightstar.clinic.persistence.MessageService;

import javax.servlet.ServletContext;

/**
 * Service operating on messages which uses jdbc.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcMessageService extends JdbcService implements MessageService {

    /**
     * Constructs <code>JdbcMessageService</code> object.
     *
     * @param context servlet context.
     */
    public JdbcMessageService(final ServletContext context) {
        super(context);
    }
}
