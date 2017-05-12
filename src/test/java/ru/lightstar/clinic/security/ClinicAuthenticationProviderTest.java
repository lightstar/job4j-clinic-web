package ru.lightstar.clinic.security;

import org.junit.Test;
import ru.lightstar.clinic.controller.ControllerTest;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.pet.Pet;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

/**
 * <code>ClinicAuthenticationProvider</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ClinicAuthenticationProviderTest extends ControllerTest {

    /**
     * Test correctness spring security login.
     */
    @Test
    public void whenLoginThenResult() throws Exception {
        final Client vasya = new Client("Vasya", Pet.NONE, 0);
        vasya.setPassword(SecurityUtil.getHashedPassword("qwerty"));
        vasya.setRole(new Role("admin"));
        when(this.mockClinicService.findClientByName("Vasya")).thenReturn(vasya);

        this.mockMvc.perform(formLogin("/login")
                .user("login", "Vasya")
                .password("password", "qwerty"))
                .andExpect(authenticated().withRoles("ADMIN"));
    }

    /**
     * Test correctness spring security login.
     */
    @Test
    public void whenLoginWithWrongPasswordThenDoNotLogin() throws Exception {
        final Client vasya = new Client("Vasya", Pet.NONE, 0);
        vasya.setPassword(SecurityUtil.getHashedPassword("qwerty"));
        when(this.mockClinicService.findClientByName("Vasya")).thenReturn(vasya);

        this.mockMvc.perform(formLogin("/login")
                .user("login", "Vasya")
                .password("password", "123456"))
                .andExpect(unauthenticated());
    }

    /**
     * Test correctness spring security login.
     */
    @Test
    public void whenLoginWithServiceExceptionThenDoNotLogin() throws Exception {
        when(this.mockClinicService.findClientByName("Vasya")).thenThrow(new ServiceException("Client not found"));

        this.mockMvc.perform(formLogin("/login")
                .user("login", "Vasya")
                .password("password", "qwerty"))
                .andExpect(unauthenticated());
    }
}