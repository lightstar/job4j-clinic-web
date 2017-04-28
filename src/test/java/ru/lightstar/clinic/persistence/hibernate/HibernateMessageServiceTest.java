package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.SessionFactory;
import org.junit.Test;
import ru.lightstar.clinic.SessionFactoryMocker;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Message;
import ru.lightstar.clinic.persistence.MessageServiceTest;

import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;
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
    private final SessionFactoryMocker hibernateMocker;

    /**
     * Constructs <code>HibernateMessageServiceTest</code> object.
     */
    public HibernateMessageServiceTest() {
        super();
        final ServletContext context = mock(ServletContext.class);

        this.hibernateMocker = new SessionFactoryMocker();
        final SessionFactory sessionFactory = this.hibernateMocker.getSessionFactory();
        when(context.getAttribute("sessionFactory")).thenReturn(sessionFactory);

        this.messageService = new HibernateMessageService(context);
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
        doThrow(PersistenceException.class).when(this.hibernateMocker.getClientMessagesQuery()).list();
        super.whenGetClientMessagesThenResult();
    }

    /**
     * Test correctness of <code>addMessage</code> method.
     */
    @Test
    public void whenAddMessageThenItAdds() throws ServiceException, SQLException {
        this.messageService.addMessage(this.client, "Some message");

        verify(this.hibernateMocker.getSession(), atLeastOnce())
                .beginTransaction();
        verify(this.hibernateMocker.getSession(), times(1))
                .save(any(Message.class));
        verify(this.hibernateMocker.getTransaction(), atLeastOnce())
                .commit();
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
        doThrow(PersistenceException.class).when(this.hibernateMocker.getSession()).save(any(Message.class));
        this.messageService.addMessage(this.client, "Some message");
    }

    /**
     * Test correctness of <code>deleteMessage</code> method.
     */
    @Test
    public void whenDeleteMessageThenItDeletes() throws ServiceException, SQLException {
        this.messageService.deleteMessage(this.client, 1);

        verify(this.hibernateMocker.getSession(), atLeastOnce())
                .beginTransaction();
        verify(this.hibernateMocker.getDeleteMessageQuery(), times(1))
                .setParameter("client", this.client);
        verify(this.hibernateMocker.getDeleteMessageQuery(), times(1))
                .setParameter("id", 1);
        verify(this.hibernateMocker.getDeleteMessageQuery(), times(1))
                .executeUpdate();
        verify(this.hibernateMocker.getTransaction(), atLeastOnce())
                .commit();
    }

    /**
     * Test correctness of <code>deleteMessage</code> method when exception is thrown.
     */
    @Test(expected = ServiceException.class)
    public void whenDeleteRoleWithExceptionThenException() throws ServiceException, SQLException {
        doThrow(PersistenceException.class).when(this.hibernateMocker.getDeleteMessageQuery()).executeUpdate();
        this.messageService.deleteMessage(this.client, 1);
    }
}