package ru.lightstar.clinic.persistence;

import org.mockito.Mockito;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Message;
import ru.lightstar.clinic.pet.Pet;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Base class for testing <code>MessageService</code> implementations.
 *
 * @author LightStar
 * @since 0.0.1
 */
public abstract class MessageServiceTest extends Mockito {

    /**
     * <code>MessageService</code> object used in all tests. Must be set in implementations.
     */
    protected MessageService messageService;

    /**
     * <code>Client</code> object used in all tests.
     */
    protected final Client client;

    /**
     * Constructs <code>MessageServiceTest</code> object.
     */
    public MessageServiceTest() {
        this.client = new Client("Vasya", Pet.NONE, 0);
        this.client.setId(1);
    }

    /**
     * Test correctness of <code>getClientMessages</code> method.
     */
    public void whenGetClientMessagesThenResult() throws ServiceException {
        final List<Message> messages = this.messageService.getClientMessages(this.client);

        assertThat(messages.size(), is(2));
        assertThat(messages.get(0).getId(), is(1));
        assertThat(messages.get(0).getClient(), is(this.client));
        assertThat(messages.get(0).getText(), is("Test message"));
        assertThat(messages.get(1).getId(), is(2));
        assertThat(messages.get(1).getClient(), is(this.client));
        assertThat(messages.get(1).getText(), is("Another test message"));
    }
}
