package ru.lightstar.clinic.servlet.message;

import org.junit.Test;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.pet.Pet;
import ru.lightstar.clinic.servlet.ServletTest;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * <code>AddMessage</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AddMessageTest extends ServletTest {

    /**
     * Test correctness of <code>doPost</code> method.
     */
    @Test
    public void whenDoPostThenResult() throws ServletException, IOException, ServiceException {
        final Client client = new Client("Vasya", Pet.NONE, 0);

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.clinicService.findClientByName("Vasya")).thenReturn(client);
        when(this.request.getParameter("text")).thenReturn("Test message");

        new AddMessage(this.clinicService, this.roleService, this.messageService).doPost(this.request, this.response);

        verify(this.messageService, atLeastOnce()).addMessage(client, "Test message");
        verify(this.response, atLeastOnce()).sendRedirect("/context/client/message?name=Vasya");
    }

    /**
     * Test correctness of <code>doPost</code> method when client name is absent.
     */
    @Test
    public void whenDoPostWithAbsentClientNameThenRedirect() throws ServletException, IOException, ServiceException {
        when(this.request.getParameter("text")).thenReturn("Test message");

        new AddMessage(this.clinicService, this.roleService, this.messageService).doPost(this.request, this.response);

        verify(this.response, atLeastOnce()).sendRedirect("/context/");
    }

    /**
     * Test correctness of <code>doPost</code> method when client not found.
     */
    @Test
    public void whenDoPostWithNotFoundClientThenRedirect() throws ServletException, IOException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getParameter("text")).thenReturn("Test message");
        when(this.clinicService.findClientByName("Vasya")).thenThrow(new ServiceException("Client not found"));

        new AddMessage(this.clinicService, this.roleService, this.messageService).doPost(this.request, this.response);

        verify(this.response, atLeastOnce()).sendRedirect("/context/");
    }

    /**
     * Test correctness of <code>doPost</code> method when message text is absent.
     */
    @Test
    public void whenDoPostWithAbsentTextThenError() throws ServletException, IOException, ServiceException {
        final Client client = new Client("Vasya", Pet.NONE, 0);

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.clinicService.findClientByName("Vasya")).thenReturn(client);

        new AddMessage(this.clinicService, this.roleService, this.messageService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(this.response, atLeastOnce()).sendRedirect("/context/client/message?name=Vasya");
    }

    /**
     * Test correctness of <code>doPost</code> method when addMessage throws exception.
     */
    @Test
    public void whenDoPostWithExceptionThenError() throws ServletException, IOException, ServiceException {
        final Client client = new Client("Vasya", Pet.NONE, 0);

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.clinicService.findClientByName("Vasya")).thenReturn(client);
        when(this.request.getParameter("text")).thenReturn("Test message");
        doThrow(new ServiceException("Test error")).when(this.messageService).addMessage(client, "Test message");

        new AddMessage(this.clinicService, this.roleService, this.messageService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(this.response, atLeastOnce()).sendRedirect("/context/client/message?name=Vasya");
    }
}