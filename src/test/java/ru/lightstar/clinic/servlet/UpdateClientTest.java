package ru.lightstar.clinic.servlet;

import org.junit.Test;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.pet.Pet;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <code>UpdateClient</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class UpdateClientTest extends ServletTest {

    /**
     * Test correctness of <code>doGet</code> method.
     */
    @Test
    public void whenDoGetThenResult() throws ServletException, IOException, ServiceException {
        final List<Role> roles = Arrays.asList(new Role("admin"), new Role("client"));
        final Client client = new Client("Vasya", Pet.NONE, 0);

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getRequestDispatcher("/WEB-INF/view/UpdateClient.jsp")).thenReturn(this.dispatcher);
        when(this.roleService.getAllRoles()).thenReturn(roles);
        when(this.clinicService.findClientByName("Vasya")).thenReturn(client);

        new UpdateClient(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.roleService, atLeastOnce()).getAllRoles();
        verify(this.clinicService, atLeastOnce()).findClientByName("Vasya");
        verify(this.request, atLeastOnce()).setAttribute("roles", roles);
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test correctness of <code>doGet</code> method when client name is absent.
     */
    @Test
    public void whenDoGetWithAbsentClientNameThenRedirect() throws ServletException, IOException, ServiceException {
        final List<Role> roles = Arrays.asList(new Role("admin"), new Role("client"));

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getRequestDispatcher("/WEB-INF/view/UpdateClient.jsp")).thenReturn(this.dispatcher);
        when(this.roleService.getAllRoles()).thenReturn(roles);
        when(this.clinicService.findClientByName("Vasya")).thenThrow(new ServiceException("Client not found"));

        new UpdateClient(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.response, atLeastOnce()).sendRedirect("/context/");
    }

    /**
     * Test correctness of <code>doGet</code> method when client not found.
     */
    @Test
    public void whenDoGetWithClientNotFoundThenRedirect() throws ServletException, IOException, ServiceException {
        final List<Role> roles = Arrays.asList(new Role("admin"), new Role("client"));

        when(this.request.getRequestDispatcher("/WEB-INF/view/UpdateClient.jsp")).thenReturn(this.dispatcher);
        when(this.roleService.getAllRoles()).thenReturn(roles);

        new UpdateClient(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.response, atLeastOnce()).sendRedirect("/context/");
    }

    /**
     * Test correctness of <code>doGet</code> method when there are no roles.
     */
    @Test
    public void whenDoGetAndNoRolesThenError() throws ServletException, IOException, ServiceException {
        final Client client = new Client("Vasya", Pet.NONE, 0);

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getRequestDispatcher("/WEB-INF/view/UpdateClient.jsp")).thenReturn(this.dispatcher);
        when(this.roleService.getAllRoles()).thenThrow(new ServiceException("Test error"));
        when(this.clinicService.findClientByName("Vasya")).thenReturn(client);

        new UpdateClient(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test updating client with correct parameters in servlet.
     */
    @Test
    public void whenDoPostThenResult()
            throws ServletException, IOException, NameException, ServiceException {
        final Role role = new Role("admin");
        role.setId(1);

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getParameter("newName")).thenReturn("Vova");
        when(this.request.getParameter("newEmail")).thenReturn("vova@mail.ru");
        when(this.request.getParameter("newPhone")).thenReturn("22222");
        when(this.request.getParameter("newRole")).thenReturn("client");
        when(this.roleService.getRoleByName("client")).thenReturn(role);

        new UpdateClient(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("message"), anyString());
        verify(this.clinicService, times(1)).updateClient("Vasya", "Vova",
                "vova@mail.ru", "22222", role);
        verify(this.response, atLeastOnce()).sendRedirect("/context/");
    }

    /**
     * Test updating client with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError()
            throws ServletException, IOException, NameException, ServiceException {

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getRequestDispatcher("/WEB-INF/view/UpdateClient.jsp")).thenReturn(this.dispatcher);
        when(this.clinicService.findClientByName("Vasya")).thenReturn(new Client("Vasya", Pet.NONE, 0));

        new UpdateClient(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test updating client when clinic service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, NameException, ServiceException {
        final Role role = new Role("admin");
        role.setId(1);

        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getParameter("newName")).thenReturn("Vova");
        when(this.request.getParameter("newEmail")).thenReturn("vova@mail.ru");
        when(this.request.getParameter("newPhone")).thenReturn("22222");
        when(this.request.getParameter("newRole")).thenReturn("client");
        when(this.request.getRequestDispatcher("/WEB-INF/view/UpdateClient.jsp")).thenReturn(this.dispatcher);
        doThrow(new ServiceException("Test error")).when(this.clinicService).updateClient("Vasya", "Vova",
                "vova@mail.ru", "22222", role);
        when(this.roleService.getRoleByName("client")).thenReturn(role);
        when(this.clinicService.findClientByName("Vasya")).thenReturn(new Client("Vasya", Pet.NONE, 0));
        when(this.request.getSession()).thenReturn(this.session);

        new UpdateClient(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }
}