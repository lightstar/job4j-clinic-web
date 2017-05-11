package ru.lightstar.clinic.controller.message;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.lightstar.clinic.controller.ControllerTest;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.pet.Pet;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>AddMessage</code> controller tests.
 *
 * @author Lightstar
 * @since 0.0.1
 */
public class AddMessageTest extends ControllerTest {

    /**
     * Test correctness of add message request.
     */
    @Test
    public void whenAddMessageThenItAdds() throws Exception {
        final Client vasya = new Client("Vasya", Pet.NONE, 0);
        when(this.mockClinicService.findClientByName("Vasya")).thenReturn(vasya);

        this.mockMvc.perform(post("/client/message/add")
                    .with(user("admin").roles("ADMIN"))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vasya")
                    .param("text", "Test message"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/client/message?name=Vasya"))
                .andExpect(redirectedUrl("/client/message?name=Vasya"))
                .andExpect(flash().attribute("message", is("Message added.")));

        verify(this.mockClinicService, times(1)).findClientByName("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
        verify(this.mockMessageService, times(1)).addMessage(vasya, "Test message");
        verifyNoMoreInteractions(this.mockMessageService);
    }

    /**
     * Test correctness of add message request when client not found.
     */
    @Test
    public void whenAddMessageAndClientNotFoundThenGoHome() throws Exception {
        when(this.mockClinicService.findClientByName("Vasya")).thenThrow(new ServiceException("Client not found"));

        this.mockMvc.perform(post("/client/message/add")
                    .with(user("admin").roles("ADMIN"))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vasya")
                    .param("text", "Test message"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"));

        verify(this.mockClinicService, times(1)).findClientByName("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
        verifyNoMoreInteractions(this.mockMessageService);
    }

    /**
     * Test correctness of add message request when <code>ServiceException</code> is thrown.
     */
    @Test
    public void whenAddMessageWithServiceExceptionThenError() throws Exception {
        final Client vasya = new Client("Vasya", Pet.NONE, 0);
        when(this.mockClinicService.findClientByName("Vasya")).thenReturn(vasya);
        doThrow(new ServiceException("Can't add message")).when(this.mockMessageService)
                .addMessage(vasya, "Test message");

        this.mockMvc.perform(post("/client/message/add")
                    .with(user("admin").roles("ADMIN"))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vasya")
                    .param("text", "Test message"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/client/message?name=Vasya"))
                .andExpect(redirectedUrl("/client/message?name=Vasya"))
                .andExpect(flash().attribute("error", is("Can't add message.")));
    }
}