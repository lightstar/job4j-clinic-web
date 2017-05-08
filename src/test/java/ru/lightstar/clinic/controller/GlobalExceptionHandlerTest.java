package ru.lightstar.clinic.controller;

import org.junit.Test;
import org.springframework.dao.NonTransientDataAccessResourceException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>GlobalExceptionHandler</code> class tests.
 *
 * @author Lightstar
 * @since 0.0.1
 */
public class GlobalExceptionHandlerTest extends ControllerTest {

    /**
     * Test correctness of <code>DataAccessException</code> handling.
     */
    @Test
    public void whenDataAccessExceptionThenRedirectToError() throws Exception {
        when(this.mockClinicService.getAllClients())
                .thenThrow(new NonTransientDataAccessResourceException("Database error"));

        this.mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/error"))
                .andExpect(redirectedUrl("/error"))
                .andExpect(flash().attribute("error", is("Database error.")));
    }

    /**
     * Test correctness of <code>RuntimeException</code> handling.
     */
    @Test
    public void whenRuntimeExceptionThenRedirectToError() throws Exception {
        when(this.mockClinicService.getAllClients())
                .thenThrow(new RuntimeException("Some error"));

        this.mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/error"))
                .andExpect(redirectedUrl("/error"))
                .andExpect(flash().attribute("error", is("Unknown error.")));
    }
}