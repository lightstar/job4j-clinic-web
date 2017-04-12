package ru.lightstar.clinic.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.Client;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.pet.Cat;
import ru.lightstar.clinic.pet.Pet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * <code>ShowClients</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowClientsTest extends Mockito {

    /**
     * Test correctness of <code>doGet</code> method without filters.
     */
    @Test
    public void whenDoGetWithoutFiltersThenResult() throws ServletException, IOException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final Client[] clients = new Client[]{new Client("Vasya", Pet.NONE, 1)};

        when(request.getParameterMap()).thenReturn(Collections.emptyMap());
        when(request.getRequestDispatcher("/WEB-INF/view/ShowClients.jsp")).thenReturn(dispatcher);
        when(clinicService.getAllClients()).thenReturn(clients);

        new ShowClients(clinicService).doGet(request, response);

        verify(clinicService, atLeastOnce()).getAllClients();
        verify(request, atLeastOnce()).setAttribute("clients", clients);
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }

    /**
     * Test correctness of <code>doGet</code> method with client name filter.
     */
    @Test
    public void whenDoGetWithClientNameFilterThenResult() throws ServletException, IOException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final Client client = new Client("Vasya", Pet.NONE, 1);

        when(request.getParameterMap()).thenReturn(Collections.singletonMap("filterName", new String[]{"Vasya"}));
        when(request.getParameter("filterName")).thenReturn("Vasya");
        when(request.getRequestDispatcher("/WEB-INF/view/ShowClients.jsp")).thenReturn(dispatcher);
        when(clinicService.findClientByName("Vasya")).thenReturn(client);

        new ShowClients(clinicService).doGet(request, response);

        verify(clinicService, atLeastOnce()).findClientByName("Vasya");
        verify(request, atLeastOnce()).setAttribute("clients", new Client[]{client});
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }

    /**
     * Test correctness of <code>doGet</code> method with client name filter when service can't found it.
     */
    @Test
    public void whenDoGetWithClientNameFilterAndClientNotFoundThenResult()
            throws ServletException, IOException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);

        when(request.getParameterMap()).thenReturn(Collections.singletonMap("filterName", new String[]{"Vasya"}));
        when(request.getParameter("filterName")).thenReturn("Vasya");
        when(request.getRequestDispatcher("/WEB-INF/view/ShowClients.jsp")).thenReturn(dispatcher);
        when(clinicService.findClientByName("Vasya")).thenThrow(new ServiceException("Client not found"));

        new ShowClients(clinicService).doGet(request, response);

        verify(clinicService, atLeastOnce()).findClientByName("Vasya");
        verify(request, atLeastOnce()).setAttribute("clients", new Client[]{});
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }

    /**
     * Test correctness of <code>doGet</code> method with pet name filter.
     */
    @Test
    public void whenDoGetWithPetNameFilterThenResult() throws ServletException, IOException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final Pet pet = new Cat("Murka", new DummyOutput());
        final Client client = new Client("Vasya", pet, 1);
        final Client[] clients = new Client[]{client};

        when(request.getParameterMap()).thenReturn(Collections.singletonMap("filterPetName", new String[]{"Murka"}));
        when(request.getParameter("filterPetName")).thenReturn("Murka");
        when(request.getRequestDispatcher("/WEB-INF/view/ShowClients.jsp")).thenReturn(dispatcher);
        when(clinicService.findClientsByPetName("Murka")).thenReturn(clients);

        new ShowClients(clinicService).doGet(request, response);

        verify(clinicService, atLeastOnce()).findClientsByPetName("Murka");
        verify(request, atLeastOnce()).setAttribute("clients", clients);
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }
}