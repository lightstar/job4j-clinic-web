package ru.lightstar.clinic.controller;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>AccessDenied</code> controller tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AccessDeniedTest extends ControllerTest {

    /**
     * Test correctness of show request.
     */
    @Test
    public void whenShowThenItShows() throws Exception {
        this.mockMvc.perform(get("/access-denied"))
                .andExpect(status().isOk())
                .andExpect(view().name("AccessDenied"))
                .andExpect(forwardedUrl("/WEB-INF/view/AccessDenied.jsp"))
                .andExpect(model().attribute("error", is("Access to this page is denied!")));
    }
}