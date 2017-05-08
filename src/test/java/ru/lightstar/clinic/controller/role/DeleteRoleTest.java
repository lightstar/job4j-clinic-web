package ru.lightstar.clinic.controller.role;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.lightstar.clinic.controller.ControllerTest;
import ru.lightstar.clinic.exception.ServiceException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>DeleteRole</code> controller tests.
 *
 * @author Lightstar
 * @since 0.0.1
 */
public class DeleteRoleTest extends ControllerTest {

    /**
     * Test correctness of delete role request.
     */
    @Test
    public void whenDeleteRoleThenItDeletes() throws Exception {
        this.mockMvc.perform(post("/role/delete")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "client"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/role"))
                .andExpect(redirectedUrl("/role"))
                .andExpect(flash().attribute("message", is("Role deleted.")));

        verify(this.mockRoleService, times(1)).deleteRole("client");
        verifyNoMoreInteractions(this.mockRoleService);
    }

    /**
     * Test correctness of delete role request when <code>ServiceException</code> is thrown.
     */
    @Test
    public void whenDeleteRoleWithServiceExceptionThenError() throws Exception {
        doThrow(new ServiceException("Can't delete role")).when(this.mockRoleService).deleteRole("client");

        this.mockMvc.perform(post("/role/delete")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "client"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/role"))
                .andExpect(redirectedUrl("/role"))
                .andExpect(flash().attribute("error", is("Can't delete role.")));
    }
}