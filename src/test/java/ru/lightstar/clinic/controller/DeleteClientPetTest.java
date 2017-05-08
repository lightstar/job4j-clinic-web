package ru.lightstar.clinic.controller;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.lightstar.clinic.exception.ServiceException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>DeleteClientPet</code> controller tests.
 *
 * @author Lightstar
 * @since 0.0.1
 */
public class DeleteClientPetTest extends ControllerTest {

    /**
     * Test correctness of delete client pet request.
     */
    @Test
    public void whenDeleteClientPetThenItDeletes() throws Exception {
        this.mockMvc.perform(post("/client/pet/delete")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vasya"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("message", is("Pet deleted.")));

        verify(this.mockClinicService, times(1)).deleteClientPet("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
    }

    /**
     * Test correctness of delete client pet request when <code>ServiceException</code> is thrown.
     */
    @Test
    public void whenDeleteClientPetWithServiceExceptionThenError() throws Exception {
        doThrow(new ServiceException("Can't delete pet")).when(this.mockClinicService).deleteClientPet("Vasya");

        this.mockMvc.perform(post("/client/pet/delete")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vasya"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("error", is("Can't delete pet.")));
    }
}