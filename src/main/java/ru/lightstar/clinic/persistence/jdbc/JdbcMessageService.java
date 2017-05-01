package ru.lightstar.clinic.persistence.jdbc;

import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Message;
import ru.lightstar.clinic.persistence.MessageService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service operating on messages which uses jdbc.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcMessageService extends JdbcService implements MessageService {

    /**
     * SQL used to get client's messages from database.
     */
    public static final String CLIENT_MESSAGES_SQL =
            "SELECT * FROM message WHERE client_id = ?";

    /**
     * SQL used to add new message to database.
     */
    public static final String ADD_MESSAGE_SQL =
            "INSERT INTO message (text, client_id) VALUES (?,?)";

    /**
     * SQL used to delete message from database.
     */
    public static final String DELETE_MESSAGE_SQL =
            "DELETE FROM message WHERE client_id = ? AND id = ?";

    /**
     * Constructs <code>JdbcMessageService</code> object.
     *
     * @param connection jdbc connection.
     */
    public JdbcMessageService(final Connection connection) {
        super(connection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message> getClientMessages(final Client client) throws ServiceException {
        final List<Message> messages = new ArrayList<>();

        try (final PreparedStatement statement = this.connection.prepareStatement(CLIENT_MESSAGES_SQL)) {
            statement.setInt(1, client.getId());
            try (final ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    final int id = rs.getInt("id");
                    final String text = rs.getString("text");

                    final Message message = new Message(client, text);
                    message.setId(id);
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            throw new ServiceException(String.format("Can't get data from database: %s", e.getMessage()));
        }

        return messages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessage(final Client client, final String text) throws ServiceException {
        if (text.isEmpty()) {
            throw new ServiceException("Text is empty");
        }

        try (final PreparedStatement statement = this.connection.prepareStatement(ADD_MESSAGE_SQL)) {
            statement.setString(1, text);
            statement.setInt(2, client.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ServiceException(String.format("Can't insert message into database: %s", e.getMessage()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMessage(final Client client, final int id) throws ServiceException {
        try (final PreparedStatement statement = this.connection.prepareStatement(DELETE_MESSAGE_SQL)) {
            statement.setInt(1, client.getId());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ServiceException(String.format("Can't delete message from database: %s", e.getMessage()));
        }
    }
}
