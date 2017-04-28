package ru.lightstar.clinic.servlet;

import org.junit.Test;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.pet.Cat;
import ru.lightstar.clinic.pet.Pet;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

/**
 * <code>ShowClients</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowClientsTest extends ServletTest {

    /**
     * Test correctness of <code>doGet</code> method without filters.
     */
    @Test
    public void whenDoGetWithoutFiltersThenResult() throws ServletException, IOException {
        final Client[] clients = new Client[]{new Client("Vasya", Pet.NONE, 1)};

        when(this.request.getParameterMap()).thenReturn(Collections.emptyMap());
        when(this.request.getRequestDispatcher("/WEB-INF/view/ShowClients.jsp")).thenReturn(this.dispatcher);
        when(this.clinicService.getAllClients()).thenReturn(clients);

        new ShowClients(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.clinicService, atLeastOnce()).getAllClients();
        verify(this.request, atLeastOnce()).setAttribute("clients", clients);
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test correctness of <code>doGet</code> method with client name filter.
     */
    @Test
    public void whenDoGetWithClientNameFilterThenResult() throws ServletException, IOException, ServiceException {
        final Client client = new Client("Vasya", Pet.NONE, 1);

        when(this.request.getParameter("filterType")).thenReturn("client");
        when(this.request.getParameter("filterName")).thenReturn("Vasya");
        when(this.request.getRequestDispatcher("/WEB-INF/view/ShowClients.jsp")).thenReturn(this.dispatcher);
        when(this.clinicService.findClientByName("Vasya")).thenReturn(client);

        new ShowClients(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.clinicService, atLeastOnce()).findClientByName("Vasya");
        verify(this.request, atLeastOnce()).setAttribute("clients", new Client[]{client});
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test correctness of <code>doGet</code> method with client name filter when service can't found it.
     */
    @Test
    public void whenDoGetWithClientNameFilterAndClientNotFoundThenResult()
            throws ServletException, IOException, ServiceException {
        when(this.request.getParameter("filterType")).thenReturn("client");
        when(this.request.getParameter("filterName")).thenReturn("Vasya");
        when(this.request.getRequestDispatcher("/WEB-INF/view/ShowClients.jsp")).thenReturn(this.dispatcher);
        when(this.clinicService.findClientByName("Vasya")).thenThrow(new ServiceException("Client not found"));

        new ShowClients(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.clinicService, atLeastOnce()).findClientByName("Vasya");
        verify(this.request, atLeastOnce()).setAttribute("clients", new Client[]{});
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test correctness of <code>doGet</code> method with pet name filter.
     */
    @Test
    public void whenDoGetWithPetNameFilterThenResult() throws ServletException, IOException, ServiceException {
        final Pet pet = new Cat("Murka", new DummyOutput());
        final Client client = new Client("Vasya", pet, 1);
        final Client[] clients = new Client[]{client};

        when(this.request.getParameter("filterType")).thenReturn("pet");
        when(this.request.getParameter("filterName")).thenReturn("Murka");
        when(this.request.getRequestDispatcher("/WEB-INF/view/ShowClients.jsp")).thenReturn(this.dispatcher);
        when(this.clinicService.findClientsByPetName("Murka")).thenReturn(clients);

        new ShowClients(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.clinicService, atLeastOnce()).findClientsByPetName("Murka");
        verify(this.request, atLeastOnce()).setAttribute("clients", clients);
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }
}