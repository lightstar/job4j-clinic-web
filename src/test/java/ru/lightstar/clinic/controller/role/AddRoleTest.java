package ru.lightstar.clinic.controller.role;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.lightstar.clinic.controller.ControllerTest;
import ru.lightstar.clinic.exception.ServiceException;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>AddRole</code> controller tests.
 *
 * @author Lightstar
 * @since 0.0.1
 */
public class AddRoleTest extends ControllerTest {

    /**
     * Test correctness of add role request.
     */
    @Test
    public void whenAddRoleThenItAdds() throws Exception {
        this.mockMvc.perform(post("/role/add")
                    .with(user("admin").roles("ADMIN"))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "client"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/role"))
                .andExpect(redirectedUrl("/role"))
                .andExpect(flash().attribute("message", is("Role added.")));

        verify(this.mockRoleService, times(1)).addRole("client");
        verifyNoMoreInteractions(this.mockRoleService);
    }

    /**
     * Test correctness of add role request when <code>ServiceException</code> is thrown.
     */
    @Test
    public void whenAddRoleWithServiceExceptionThenError() throws Exception {
        doThrow(new ServiceException("Can't add role")).when(this.mockRoleService).addRole("client");

        this.mockMvc.perform(post("/role/add")
                    .with(user("admin").roles("ADMIN"))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "client"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/role"))
                .andExpect(redirectedUrl("/role"))
                .andExpect(flash().attribute("error", is("Can't add role.")));
    }
}