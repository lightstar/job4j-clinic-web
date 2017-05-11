package ru.lightstar.clinic.controller;

import org.junit.Test;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.pet.Bird;
import ru.lightstar.clinic.pet.Cat;
import ru.lightstar.clinic.pet.Pet;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>ShowClients</code> controller tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowClientsTest extends ControllerTest {

    /**
     * Test correctness of show request without filters.
     */
    @Test
    public void whenShowThenResult() throws Exception {
        final Client vasya = new Client("Vasya", Pet.NONE, 0);
        final Client vova = new Client("Vova", Pet.NONE, 1);
        when(this.mockClinicService.getAllClients()).thenReturn(new Client[]{vasya, vova});

        this.mockMvc.perform(get("/")
                    .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("ShowClients"))
                .andExpect(forwardedUrl("/WEB-INF/view/ShowClients.jsp"))
                .andExpect(model().attribute("clients", arrayWithSize(2)))
                .andExpect(model().attribute("clients", arrayContaining(vasya, vova)));

        verify(this.mockClinicService, times(1)).getAllClients();
        verifyNoMoreInteractions(this.mockClinicService);
    }

    /**
     * Test correctness of show request with filter by client name.
     */
    @Test
    public void whenShowWithFilterByClientThenResult() throws Exception {
        final Client vasya = new Client("Vasya", Pet.NONE, 0);
        when(this.mockClinicService.findClientByName("Vasya")).thenReturn(vasya);

        this.mockMvc.perform(get("/")
                    .with(user("admin").roles("ADMIN"))
                    .param("filterType", "client")
                    .param("filterName", "Vasya"))
                .andExpect(status().isOk())
                .andExpect(view().name("ShowClients"))
                .andExpect(forwardedUrl("/WEB-INF/view/ShowClients.jsp"))
                .andExpect(model().attribute("clients", arrayWithSize(1)))
                .andExpect(model().attribute("clients", arrayContaining(vasya)));

        verify(this.mockClinicService, times(1)).findClientByName("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
    }

    /**
     * Test correctness of show request with filter by client name when client not found.
     */
    @Test
    public void whenShowWithFilterByClientAndClientNotFoundThenResult() throws Exception {
        when(this.mockClinicService.findClientByName("Vasya")).thenThrow(new ServiceException("Client not found"));

        this.mockMvc.perform(get("/")
                    .with(user("admin").roles("ADMIN"))
                    .param("filterType", "client")
                    .param("filterName", "Vasya"))
                .andExpect(status().isOk())
                .andExpect(view().name("ShowClients"))
                .andExpect(forwardedUrl("/WEB-INF/view/ShowClients.jsp"))
                .andExpect(model().attribute("clients", emptyArray()));

        verify(this.mockClinicService, times(1)).findClientByName("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
    }

    /**
     * Test correctness of show request with filter by pet name.
     */
    @Test
    public void whenShowWithFilterByPetThenResult() throws Exception {
        final Client vasya = new Client("Vasya", new Cat("Murka", new DummyOutput()), 0);
        final Client vova = new Client("Vova", new Bird("Murka", new DummyOutput()), 1);
        when(this.mockClinicService.findClientsByPetName("Murka")).thenReturn(new Client[]{vasya, vova});

        this.mockMvc.perform(get("/")
                    .with(user("admin").roles("ADMIN"))
                    .param("filterType", "pet")
                    .param("filterName", "Murka"))
                .andExpect(status().isOk())
                .andExpect(view().name("ShowClients"))
                .andExpect(forwardedUrl("/WEB-INF/view/ShowClients.jsp"))
                .andExpect(model().attribute("clients", arrayWithSize(2)))
                .andExpect(model().attribute("clients", arrayContaining(vasya, vova)));

        verify(this.mockClinicService, times(1)).findClientsByPetName("Murka");
        verifyNoMoreInteractions(this.mockClinicService);
    }
}