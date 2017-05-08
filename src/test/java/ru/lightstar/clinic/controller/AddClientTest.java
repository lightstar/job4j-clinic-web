package ru.lightstar.clinic.controller;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Role;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>AddClient</code> controller tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AddClientTest extends ControllerTest {

    /**
     * Test correctness of show form request.
     */
    @Test
    public void whenShowFormThenItShows() throws Exception {
        final Role adminRole = new Role("admin");
        final Role clientRole = new Role("client");
        when(this.mockRoleService.getAllRoles()).thenReturn(Arrays.asList(adminRole, clientRole));

        this.mockMvc.perform(get("/client/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("AddClient"))
                .andExpect(forwardedUrl("/WEB-INF/view/AddClient.jsp"))
                .andExpect(model().attribute("roles", hasSize(2)))
                .andExpect(model().attribute("roles",
                        hasItem(hasProperty("name", is("admin")))))
                .andExpect(model().attribute("roles",
                        hasItem(hasProperty("name", is("client")))));

        verify(this.mockRoleService, times(1)).getAllRoles();
        verifyNoMoreInteractions(this.mockRoleService);
    }

    /**
     * Test correctness of add client request.
     */
    @Test
    public void whenAddClientThenItAdds() throws Exception {
        final Role clientRole = new Role("client");
        when(this.mockRoleService.getRoleByName("client")).thenReturn(clientRole);

        this.mockMvc.perform(post("/client/add")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vova")
                    .param("pos", "2")
                    .param("email", "vova@mail.ru")
                    .param("phone", "123456")
                    .param("role", "client"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("message", is("Client added.")));

        verify(this.mockRoleService, times(1)).getRoleByName("client");
        verifyNoMoreInteractions(this.mockRoleService);

        verify(this.mockClinicService, times(1)).addClient(1, "Vova",
                "vova@mail.ru", "123456", clientRole);
        verifyNoMoreInteractions(this.mockClinicService);
    }

    /**
     * Test correctness of add client request when <code>ServiceException</code> is thrown.
     */
    @Test
    public void whenAddClientWithServiceExceptionThenError() throws Exception {
        when(this.mockRoleService.getRoleByName("client")).thenThrow(new ServiceException("Role not found"));

        this.mockMvc.perform(post("/client/add")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vova")
                    .param("pos", "2")
                    .param("email", "vova@mail.ru")
                    .param("phone", "123456")
                    .param("role", "client"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/client/add?pos=2"))
                .andExpect(redirectedUrl("/client/add?pos=2"))
                .andExpect(flash().attribute("error", is("Role not found.")));
    }

    /**
     * Test correctness of add client request when parameters are invalid.
     */
    @Test
    public void whenAddClientWithInvalidParametersThenError() throws Exception {
        this.mockMvc.perform(post("/client/add")
                    .param("pos", "aaa"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/client/add?pos=aaa"))
                .andExpect(redirectedUrl("/client/add?pos=aaa"))
                .andExpect(flash().attribute("error", is("Invalid parameters.")));
    }
}