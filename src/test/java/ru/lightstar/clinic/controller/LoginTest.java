package ru.lightstar.clinic.controller;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>Login</code> controller tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class LoginTest extends ControllerTest {

    /**
     * Test correctness of show form request.
     */
    @Test
    public void whenShowFormThenItShows() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("Login"))
                .andExpect(forwardedUrl("/WEB-INF/view/Login.jsp"));
    }

    /**
     * Test correctness of show form request with error param.
     */
    @Test
    public void whenShowFormWithErrorParamThenItShows() throws Exception {
        this.mockMvc.perform(get("/login")
                .param("error",""))
                .andExpect(status().isOk())
                .andExpect(view().name("Login"))
                .andExpect(forwardedUrl("/WEB-INF/view/Login.jsp"))
                .andExpect(model().attribute("error", is("Wrong login or password!")));
    }

    /**
     * Test correctness of show form request with logout param.
     */
    @Test
    public void whenShowFormWithLogoutParamThenItShows() throws Exception {
        this.mockMvc.perform(get("/login")
                .param("logout",""))
                .andExpect(status().isOk())
                .andExpect(view().name("Login"))
                .andExpect(forwardedUrl("/WEB-INF/view/Login.jsp"))
                .andExpect(model().attribute("message", is("Logout successful.")));
    }
}