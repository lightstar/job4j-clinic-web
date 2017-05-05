package ru.lightstar.clinic.persistence;

import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Message;

import java.util.List;

/**
 * Service operating on messages.
 *
 * @author LightStar
 * @since 0.0.1
 */
public interface MessageService {

    /**
     * Get all client's messages.
     *
     * @param client client object.
     * @return list of client's messages.
     */
    List<Message> getClientMessages(Client client);

    /**
     * Add new client's message.
     *
     * @param client client object.
     * @param text message's text.
     * @throws ServiceException thrown if can't add message.
     */
    void addMessage(Client client, String text) throws ServiceException;

    /**
     * Delete client's message.
     *
     * @param client client object.
     * @param id message's id.
     */
    void deleteMessage(Client client, int id);
}
