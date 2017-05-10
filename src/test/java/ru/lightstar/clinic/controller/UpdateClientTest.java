package ru.lightstar.clinic.controller;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.pet.Pet;
import ru.lightstar.clinic.security.SecurityUtil;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>UpdateClient</code> controller tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class UpdateClientTest extends ControllerTest {

    /**
     * Test correctness of show form request.
     */
    @Test
    public void whenShowFormThenResult() throws Exception {
        final Role adminRole = new Role("admin");
        final Role clientRole = new Role("client");
        when(this.mockRoleService.getAllRoles()).thenReturn(Arrays.asList(adminRole, clientRole));

        final Client vasya = new Client("Vasya", Pet.NONE, 0);
        vasya.setEmail("vasya@mail.ru");
        vasya.setPhone("123456");
        vasya.setRole(clientRole);
        when(this.mockClinicService.findClientByName("Vasya")).thenReturn(vasya);

        this.mockMvc.perform(get("/client/update")
                    .param("name", "Vasya"))
                .andExpect(status().isOk())
                .andExpect(view().name("UpdateClient"))
                .andExpect(forwardedUrl("/WEB-INF/view/UpdateClient.jsp"))
                .andExpect(model().attribute("roles", hasSize(2)))
                .andExpect(model().attribute("roles",
                        hasItem(hasProperty("name", is("admin")))))
                .andExpect(model().attribute("roles",
                        hasItem(hasProperty("name", is("client")))))
                .andExpect(model().attribute("newName", is("Vasya")))
                .andExpect(model().attribute("newEmail", is("vasya@mail.ru")))
                .andExpect(model().attribute("newPhone", is("123456")))
                .andExpect(model().attribute("newRole", is("client")));

        verify(this.mockRoleService, times(1)).getAllRoles();
        verifyNoMoreInteractions(this.mockRoleService);
        verify(this.mockClinicService, times(1)).findClientByName("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
    }

    /**
     * Test correctness of show form request when client not found.
     */
    @Test
    public void whenShowAndClientNotFoundThenGoHome() throws Exception {
        when(this.mockClinicService.findClientByName("Vasya")).thenThrow(new ServiceException("Client not found"));

        this.mockMvc.perform(get("/client/update")
                    .param("name", "Vasya"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"));

        verify(this.mockClinicService, times(1)).findClientByName("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
    }

    /**
     * Test correctness of update client request.
     */
    @Test
    public void whenUpdateClientThenItUpdates() throws Exception {
        final Role adminRole = new Role("admin");
        when(this.mockRoleService.getRoleByName("admin")).thenReturn(adminRole);

        this.mockMvc.perform(post("/client/update")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vasya")
                    .param("newName", "Vova")
                    .param("newEmail", "vova@mail.ru")
                    .param("newPhone", "55555")
                    .param("newRole", "admin")
                    .param("newPassword", "qwerty"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("message", is("Client updated.")));

        verify(this.mockRoleService, times(1)).getRoleByName("admin");
        verifyNoMoreInteractions(this.mockRoleService);
        verify(this.mockClinicService, times(1)).updateClient("Vasya","Vova",
                "vova@mail.ru", "55555", adminRole, SecurityUtil.getHashedPassword("qwerty"));
        verifyNoMoreInteractions(this.mockClinicService);
    }

    /**
     * Test correctness of update client request when <code>ServiceException</code> is thrown.
     */
    @Test
    public void whenUpdateClientWithServiceExceptionThenError() throws Exception {
        when(this.mockRoleService.getRoleByName("admin")).thenThrow(new ServiceException("Role not found"));

        this.mockMvc.perform(post("/client/update")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vasya")
                    .param("newName", "Vova")
                    .param("newEmail", "vova@mail.ru")
                    .param("newPhone", "55555")
                    .param("newRole", "admin"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/client/update?name=Vasya"))
                .andExpect(redirectedUrl("/client/update?name=Vasya"))
                .andExpect(flash().attribute("error", is("Role not found.")));
    }
}