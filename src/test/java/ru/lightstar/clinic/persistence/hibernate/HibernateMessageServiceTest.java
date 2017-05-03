package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.hibernate5.HibernateTemplate;
import ru.lightstar.clinic.SessionFactoryMocker;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Message;
import ru.lightstar.clinic.persistence.MessageServiceTest;

import java.sql.SQLException;

/**
 * Class <code>HibernateMessageService</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class HibernateMessageServiceTest extends MessageServiceTest {

    /**
     * Helper object used to mock hibernate session factory.
     */
    private SessionFactoryMocker hibernateMocker;

    /**
     * Initialize test.
     */
    @Before
    public void initTest() {
        this.hibernateMocker = new SessionFactoryMocker();
        final SessionFactory sessionFactory = this.hibernateMocker.getSessionFactory();
        final HibernateTemplate hibernateTemplate = new HibernateTemplate();
        hibernateTemplate.setSessionFactory(sessionFactory);
        this.messageService = new HibernateMessageService(hibernateTemplate);
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenGetClientMessagesThenResult() throws ServiceException {
        super.whenGetClientMessagesThenResult();
    }

    /**
     * Test correctness of <code>getClientMessages</code> method when exception is thrown.
     */
    @Test(expected = ServiceException.class)
    public void whenGetClientMessagesWithExceptionThenException() throws ServiceException {
        doThrow(HibernateException.class).when(this.hibernateMocker.getClientMessagesQuery()).list();
        super.whenGetClientMessagesThenResult();
    }

    /**
     * Test correctness of <code>addMessage</code> method.
     */
    @Test
    public void whenAddMessageThenItAdds() throws ServiceException, SQLException {
        this.messageService.addMessage(this.client, "Some message");
        verify(this.hibernateMocker.getSession(), times(1))
                .save(any(Message.class));
    }

    /**
     * Test correctness of <code>addMessage</code> method when text is empty.
     */
    @Test(expected = ServiceException.class)
    public void whenAddMessageWithEmptyTextThenException() throws ServiceException, SQLException {
        this.messageService.addMessage(this.client, "");
    }

    /**
     * Test correctness of <code>addMessage</code> method when exception is thrown.
     */
    @Test(expected = ServiceException.class)
    public void whenAddMessageWithExceptionThenException() throws ServiceException, SQLException {
        doThrow(HibernateException.class).when(this.hibernateMocker.getSession()).save(any(Message.class));
        this.messageService.addMessage(this.client, "Some message");
    }

    /**
     * Test correctness of <code>deleteMessage</code> method.
     */
    @Test
    public void whenDeleteMessageThenItDeletes() throws ServiceException, SQLException {
        this.messageService.deleteMessage(this.client, 1);

        verify(this.hibernateMocker.getDeleteMessageQuery(), times(1))
                .setParameter("client", this.client);
        verify(this.hibernateMocker.getDeleteMessageQuery(), times(1))
                .setParameter("id", 1);
        verify(this.hibernateMocker.getDeleteMessageQuery(), times(1))
                .executeUpdate();
    }

    /**
     * Test correctness of <code>deleteMessage</code> method when exception is thrown.
     */
    @Test(expected = ServiceException.class)
    public void whenDeleteRoleWithExceptionThenException() throws ServiceException, SQLException {
        doThrow(HibernateException.class).when(this.hibernateMocker.getDeleteMessageQuery()).executeUpdate();
        this.messageService.deleteMessage(this.client, 1);
    }
}