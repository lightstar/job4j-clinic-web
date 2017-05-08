package ru.lightstar.clinic.controller.message;

import org.junit.Test;
import ru.lightstar.clinic.controller.ControllerTest;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Message;
import ru.lightstar.clinic.pet.Pet;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>ShowMessages</code> controller tests.
 *
 * @author Lightstar
 * @since 0.0.1
 */
public class ShowMessagesTest extends ControllerTest {

    /**
     * Test correctness of show request.
     */
    @Test
    public void whenShowThenItShows() throws Exception {
        final Client vasya = new Client("Vasya", Pet.NONE, 0);
        when(this.mockClinicService.findClientByName("Vasya")).thenReturn(vasya);
        final List<Message> messages = Arrays.asList(new Message(vasya, "Test message"),
                new Message(vasya, "Another test message"));
        when(this.mockMessageService.getClientMessages(vasya)).thenReturn(messages);

        this.mockMvc.perform(get("/client/message")
                    .param("name", "Vasya"))
                .andExpect(status().isOk())
                .andExpect(view().name("ShowMessages"))
                .andExpect(forwardedUrl("/WEB-INF/view/ShowMessages.jsp"))
                .andExpect(model().attribute("messages", hasSize(2)))
                .andExpect(model().attribute("messages", hasItem(allOf(
                        hasProperty("client", is(vasya)),
                        hasProperty("text", is("Test message"))))))
                .andExpect(model().attribute("messages", hasItem(allOf(
                        hasProperty("client", is(vasya)),
                        hasProperty("text", is("Another test message"))))));

        verify(this.mockClinicService, times(1)).findClientByName("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
        verify(this.mockMessageService, times(1)).getClientMessages(vasya);
        verifyNoMoreInteractions(this.mockMessageService);
    }

    /**
     * Test correctness of show request when client not found.
     */
    @Test
    public void whenShowAndClientNotFoundThenGoHome() throws Exception {
        when(this.mockClinicService.findClientByName("Vasya")).thenThrow(new ServiceException("Client not found"));

        this.mockMvc.perform(get("/client/message")
                    .param("name", "Vasya"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"));

        verify(this.mockClinicService, times(1)).findClientByName("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
    }
}