package ru.lightstar.clinic.controller;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.lightstar.clinic.exception.ServiceException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>DeleteClient</code> controller tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class DeleteClientTest extends ControllerTest {

    /**
     * Test correctness of delete client request.
     */
    @Test
    public void whenDeleteClientThenItDeletes() throws Exception {
        this.mockMvc.perform(post("/client/delete")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vasya"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("message", is("Client deleted.")));

        verify(this.mockClinicService, times(1)).deleteClient("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
    }

    /**
     * Test correctness of delete client request when <code>ServiceException</code> is thrown.
     */
    @Test
    public void whenDeleteClientWithServiceExceptionThenError() throws Exception {
        doThrow(new ServiceException("Can't delete client")).when(this.mockClinicService).deleteClient("Vasya");

        this.mockMvc.perform(post("/client/delete")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vasya"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("error", is("Can't delete client.")));
    }
}