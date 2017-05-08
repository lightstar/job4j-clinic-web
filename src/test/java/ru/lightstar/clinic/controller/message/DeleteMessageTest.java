package ru.lightstar.clinic.controller.message;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.lightstar.clinic.controller.ControllerTest;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.pet.Pet;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>DeleteMessage</code> controller tests.
 *
 * @author Lightstar
 * @since 0.0.1
 */
public class DeleteMessageTest extends ControllerTest {

    /**
     * Test correctness of delete message request.
     */
    @Test
    public void whenDeleteMessageThenItDeletes() throws Exception {
        final Client vasya = new Client("Vasya", Pet.NONE, 0);
        when(this.mockClinicService.findClientByName("Vasya")).thenReturn(vasya);

        this.mockMvc.perform(post("/client/message/delete")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vasya")
                    .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/client/message?name=Vasya"))
                .andExpect(redirectedUrl("/client/message?name=Vasya"))
                .andExpect(flash().attribute("message", is("Message deleted.")));

        verify(this.mockClinicService, times(1)).findClientByName("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
        verify(this.mockMessageService, times(1)).deleteMessage(vasya, 1);
        verifyNoMoreInteractions(this.mockMessageService);
    }

    /**
     * Test correctness of delete message request when client not found.
     */
    @Test
    public void whenDeleteMessageAndClientNotFoundThenGoHome() throws Exception {
        when(this.mockClinicService.findClientByName("Vasya")).thenThrow(new ServiceException("Client not found"));

        this.mockMvc.perform(post("/client/message/delete")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vasya")
                    .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"));

        verify(this.mockClinicService, times(1)).findClientByName("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
        verifyNoMoreInteractions(this.mockMessageService);
    }
}