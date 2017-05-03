package ru.lightstar.clinic.servlet.message;

import org.junit.Test;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.pet.Pet;
import ru.lightstar.clinic.servlet.ServletTest;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * <code>DeleteMessage</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class DeleteMessageTest extends ServletTest {

    /**
     * Test correctness of <code>doPost</code> method.
     */
    @Test
    public void whenDoPostThenResult() throws ServletException, IOException, ServiceException {
        final Client client = new Client("Vasya", Pet.NONE, 0);

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.clinicService.findClientByName("Vasya")).thenReturn(client);
        when(this.request.getParameter("id")).thenReturn("1");

        new DeleteMessage(this.clinicService, this.roleService, this.messageService)
                .doPost(this.request, this.response);

        verify(this.messageService, atLeastOnce()).deleteMessage(client, 1);
        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/client/message?name=Vasya");
    }

    /**
     * Test correctness of <code>doPost</code> method when client name is absent.
     */
    @Test
    public void whenDoPostWithAbsentClientNameThenRedirect() throws ServletException, IOException, ServiceException {
        when(this.request.getParameter("id")).thenReturn("1");

        new DeleteMessage(this.clinicService, this.roleService, this.messageService)
                .doPost(this.request, this.response);

        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/");
    }

    /**
     * Test correctness of <code>doPost</code> method when client not found.
     */
    @Test
    public void whenDoPostWithNotFoundClientThenRedirect() throws ServletException, IOException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getParameter("id")).thenReturn("1");
        when(this.clinicService.findClientByName("Vasya")).thenThrow(new ServiceException("Client not found"));

        new DeleteMessage(this.clinicService, this.roleService, this.messageService)
                .doPost(this.request, this.response);

        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/");
    }

    /**
     * Test correctness of <code>doPost</code> method when message id is absent.
     */
    @Test
    public void whenDoPostWithAbsentIdThenError() throws ServletException, IOException, ServiceException {
        final Client client = new Client("Vasya", Pet.NONE, 0);

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.clinicService.findClientByName("Vasya")).thenReturn(client);

        new DeleteMessage(this.clinicService, this.roleService, this.messageService)
                .doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/client/message?name=Vasya");
    }

    /**
     * Test correctness of <code>doPost</code> method when message id is not a number.
     */
    @Test
    public void whenDoPostWithIdNotANumberThenError() throws ServletException, IOException, ServiceException {
        final Client client = new Client("Vasya", Pet.NONE, 0);

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getParameter("id")).thenReturn("not-a-number");
        when(this.clinicService.findClientByName("Vasya")).thenReturn(client);

        new DeleteMessage(this.clinicService, this.roleService, this.messageService)
                .doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/client/message?name=Vasya");
    }

    /**
     * Test correctness of <code>doPost</code> method when deleteMessage throws exception.
     */
    @Test
    public void whenDoPostWithExceptionThenError() throws ServletException, IOException, ServiceException {
        final Client client = new Client("Vasya", Pet.NONE, 0);

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.clinicService.findClientByName("Vasya")).thenReturn(client);
        when(this.request.getParameter("id")).thenReturn("1");
        doThrow(new ServiceException("Test error")).when(this.messageService).deleteMessage(client, 1);

        new DeleteMessage(this.clinicService, this.roleService, this.messageService)
                .doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/client/message?name=Vasya");
    }
}