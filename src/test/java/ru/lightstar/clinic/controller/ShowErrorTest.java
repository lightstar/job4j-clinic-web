package ru.lightstar.clinic.controller;

import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>ShowError</code> controller tests.
 *
 * @author Lightstar
 * @since 0.0.1
 */
public class ShowErrorTest extends ControllerTest {

    /**
     * Test correctness of show request.
     */
    @Test
    public void whenShowThenItShows() throws Exception {
        this.mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("ShowError"))
                .andExpect(forwardedUrl("/WEB-INF/view/ShowError.jsp"));
    }
}