package ru.lightstar.clinic.controller.role;

import org.junit.Test;
import ru.lightstar.clinic.controller.ControllerTest;
import ru.lightstar.clinic.model.Role;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>ShowRoles</code> controller tests.
 *
 * @author Lightstar
 * @since 0.0.1
 */
public class ShowRolesTest extends ControllerTest {

    /**
     * Test correctness of show request.
     */
    @Test
    public void whenShowThenItShows() throws Exception {
        final Role adminRole = new Role("admin");
        final Role clientRole = new Role("client");
        when(this.mockRoleService.getAllRoles()).thenReturn(Arrays.asList(adminRole, clientRole));

        this.mockMvc.perform(get("/role"))
                .andExpect(status().isOk())
                .andExpect(view().name("ShowRoles"))
                .andExpect(forwardedUrl("/WEB-INF/view/ShowRoles.jsp"))
                .andExpect(model().attribute("roles", hasSize(2)))
                .andExpect(model().attribute("roles",
                        hasItem(hasProperty("name", is("admin")))))
                .andExpect(model().attribute("roles",
                        hasItem(hasProperty("name", is("client")))));

        verify(this.mockRoleService, times(1)).getAllRoles();
        verifyNoMoreInteractions(this.mockRoleService);
    }
}