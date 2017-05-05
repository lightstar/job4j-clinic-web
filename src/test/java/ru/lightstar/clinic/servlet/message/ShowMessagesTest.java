package ru.lightstar.clinic.servlet.message;

import org.junit.Test;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Message;
import ru.lightstar.clinic.pet.Pet;
import ru.lightstar.clinic.servlet.ServletTest;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <code>ShowMessages</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowMessagesTest extends ServletTest {

    /**
     * Test correctness of <code>doGet</code> method.
     */
    @Test
    public void whenDoGetThenResult() throws ServletException, IOException, ServiceException {
        final Client client = new Client("Vasya", Pet.NONE, 0);
        final List<Message> messages = Arrays.asList(new Message(client, "Test message"),
                new Message(client, "Another test message"));

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getRequestDispatcher("/WEB-INF/view/ShowMessages.jsp")).thenReturn(this.dispatcher);
        when(this.clinicService.findClientByName("Vasya")).thenReturn(client);
        when(this.messageService.getClientMessages(client)).thenReturn(messages);

        new ShowMessages(this.clinicService, this.roleService, this.messageService).doGet(this.request, this.response);

        verify(this.messageService, atLeastOnce()).getClientMessages(client);
        verify(this.request, atLeastOnce()).setAttribute("messages", messages);
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test correctness of <code>doGet</code> method when exception is thrown.
     */
    @Test
    public void whenDoGetWithExceptionThenResult() throws ServletException, IOException, ServiceException {
        final Client client = new Client("Vasya", Pet.NONE, 0);

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getRequestDispatcher("/WEB-INF/view/ShowMessages.jsp")).thenReturn(this.dispatcher);
        when(this.clinicService.findClientByName("Vasya")).thenReturn(client);
        when(this.messageService.getClientMessages(client)).thenThrow(new RuntimeException("Some error"));

        new ShowMessages(this.clinicService, this.roleService, this.messageService).doGet(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute("error", "Unknown error.");
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test correctness of <code>doGet</code> method when client name is absent.
     */
    @Test
    public void whenDoGetWithAbsentClientNameThenRedirect() throws ServletException, IOException, ServiceException {
        when(this.request.getRequestDispatcher("/WEB-INF/view/ShowMessages.jsp")).thenReturn(this.dispatcher);

        new ShowMessages(this.clinicService, this.roleService, this.messageService).doGet(this.request, this.response);

        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/");
    }

    /**
     * Test correctness of <code>doGet</code> method when client not found.
     */
    @Test
    public void whenDoGetWhenClientNotFoundThenRedirect() throws ServletException, IOException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getRequestDispatcher("/WEB-INF/view/ShowMessages.jsp")).thenReturn(this.dispatcher);
        when(this.clinicService.findClientByName("Vasya")).thenThrow(new ServiceException("Client not found"));

        new ShowMessages(this.clinicService, this.roleService, this.messageService).doGet(this.request, this.response);

        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/");
    }
}
