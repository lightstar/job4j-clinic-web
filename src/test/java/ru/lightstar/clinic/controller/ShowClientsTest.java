package ru.lightstar.clinic.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.pet.Pet;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>ShowClients</code> controller tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-test-context.xml", "classpath:spring-web-context.xml"})
@WebAppConfiguration
public class ShowClientsTest extends Mockito {

    /**
     * Mock spring MVC runner.
     */
    private MockMvc mockMvc;

    /**
     * Mocked clinic service.
     */
    @Autowired
    private ClinicService mockClinicService;

    /**
     * Application context used for mvc tests.
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * Setting up test environment.
     */
    @Before
    public void setUp() {
        reset(this.mockClinicService);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    /**
     * Test correctness of show request without filters.
     */
    @Test
    public void whenShowThenResult() throws Exception {
        final Client vasya = new Client("Vasya", Pet.NONE, 0);
        final Client vova = new Client("Vova", Pet.NONE, 1);
        final Client[] clients = new Client[]{vasya, vova};

        when(this.mockClinicService.getAllClients()).thenReturn(clients);

        this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(view().name("ShowClients"))
                .andExpect(forwardedUrl("/WEB-INF/view/ShowClients.jsp"))
                .andExpect(model().attribute("clients", is(clients)));

        verify(this.mockClinicService, times(1)).getAllClients();
        verifyNoMoreInteractions(this.mockClinicService);
    }
}