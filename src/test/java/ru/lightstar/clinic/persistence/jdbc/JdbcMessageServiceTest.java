package ru.lightstar.clinic.persistence.jdbc;

import org.junit.Test;
import ru.lightstar.clinic.JdbcConnectionMocker;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.MessageServiceTest;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <code>JdbcMessageService</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcMessageServiceTest extends MessageServiceTest {

    /**
     * Helper object used to mock jdbc connection.
     */
    private final JdbcConnectionMocker jdbcMocker;

    /**
     * Constructs <code>JdbcMessageServiceTest</code> object.
     */
    public JdbcMessageServiceTest() {
        super();
        this.jdbcMocker = new JdbcConnectionMocker();
        final Connection connection = jdbcMocker.getConnection();
        this.messageService = new JdbcMessageService(connection);
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
    public void whenGetClientMessagesWithExceptionThenException() throws ServiceException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getClientMessagesResultSet()).next();
        super.whenGetClientMessagesThenResult();
    }

    /**
     * Test correctness of <code>addMessage</code> method.
     */
    @Test
    public void whenAddMessageThenItAdds() throws ServiceException, SQLException {
        this.messageService.addMessage(this.client, "Some message");

        verify(this.jdbcMocker.getAddMessageStatement(), times(1))
                .setString(1, "Some message");
        verify(this.jdbcMocker.getAddMessageStatement(), times(1))
                .setInt(2, this.client.getId());
        verify(this.jdbcMocker.getAddMessageStatement(), times(1))
                .executeUpdate();
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
        doThrow(SQLException.class).when(this.jdbcMocker.getAddMessageStatement()).executeUpdate();
        this.messageService.addMessage(this.client, "Some message");
    }

    /**
     * Test correctness of <code>deleteMessage</code> method.
     */
    @Test
    public void whenDeleteMessageThenItDeletes() throws ServiceException, SQLException {
        this.messageService.deleteMessage(this.client, 1);

        verify(this.jdbcMocker.getDeleteMessageStatement(), times(1))
                .setInt(1, this.client.getId());
        verify(this.jdbcMocker.getDeleteMessageStatement(), times(1))
                .setInt(2, 1);
        verify(this.jdbcMocker.getDeleteMessageStatement(), times(1))
                .executeUpdate();
    }

    /**
     * Test correctness of <code>deleteMessage</code> method when exception is thrown.
     */
    @Test(expected = ServiceException.class)
    public void whenDeleteRoleWithExceptionThenException() throws ServiceException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getDeleteMessageStatement()).executeUpdate();
        this.messageService.deleteMessage(this.client, 1);
    }
}