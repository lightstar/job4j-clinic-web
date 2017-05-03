package ru.lightstar.clinic.servlet;

import org.junit.Test;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Role;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <code>AddClient</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AddClientTest extends ServletTest {

    /**
     * Test correctness of <code>doGet</code> method.
     */
    @Test
    public void whenDoGetThenResult() throws ServletException, IOException, ServiceException {
        final List<Role> roles = Arrays.asList(new Role("admin"), new Role("client"));
        when(this.request.getRequestDispatcher("/WEB-INF/view/AddClient.jsp")).thenReturn(this.dispatcher);
        when(this.roleService.getAllRoles()).thenReturn(roles);

        new AddClient(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.roleService, atLeastOnce()).getAllRoles();
        verify(this.request, atLeastOnce()).setAttribute("roles", roles);
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test correctness of <code>doGet</code> method when there are no roles.
     */
    @Test
    public void whenDoGetAndNoRolesThenError() throws ServletException, IOException, ServiceException {
        when(this.request.getRequestDispatcher("/WEB-INF/view/AddClient.jsp")).thenReturn(this.dispatcher);
        when(this.roleService.getAllRoles()).thenThrow(new ServiceException("Test error"));

        new AddClient(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test adding client with correct parameters in servlet.
     */
    @Test
    public void whenDoPostThenResult()
            throws ServletException, IOException, NameException, ServiceException {
        final Role role = new Role("client");
        role.setId(2);

        when(this.request.getParameter("pos")).thenReturn("1");
        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getParameter("email")).thenReturn("vasya@mail.ru");
        when(this.request.getParameter("phone")).thenReturn("2323");
        when(this.request.getParameter("role")).thenReturn("client");
        when(this.roleService.getRoleByName("client")).thenReturn(role);

        new AddClient(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("message"), anyString());
        verify(this.clinicService, times(1)).addClient(0, "Vasya",
                "vasya@mail.ru", "2323", role);
        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/");
    }

    /**
     * Test adding client with position not a number in servlet.
     */
    @Test
    public void whenDoPostWithPositionNotANumberThenError()
            throws ServletException, IOException, NameException, ServiceException {
        final Role role = new Role("client");
        role.setId(2);

        when(this.request.getParameter("pos")).thenReturn("not-a-number");
        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getParameter("email")).thenReturn("vasya@mail.ru");
        when(this.request.getParameter("phone")).thenReturn("2323");
        when(this.request.getParameter("role")).thenReturn("client");
        when(this.roleService.getRoleByName("client")).thenReturn(role);

        when(this.request.getRequestDispatcher("/WEB-INF/view/AddClient.jsp")).thenReturn(this.dispatcher);

        new AddClient(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test adding client with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError()
            throws ServletException, IOException, NameException, ServiceException {

        when(this.request.getRequestDispatcher("/WEB-INF/view/AddClient.jsp")).thenReturn(this.dispatcher);

        new AddClient(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test adding client when clinic service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, NameException, ServiceException {
        final Role role = new Role("client");
        role.setId(2);

        when(this.request.getParameter("pos")).thenReturn("1");
        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getParameter("email")).thenReturn("vasya@mail.ru");
        when(this.request.getParameter("phone")).thenReturn("2323");
        when(this.request.getParameter("role")).thenReturn("client");
        when(this.request.getRequestDispatcher("/WEB-INF/view/AddClient.jsp")).thenReturn(this.dispatcher);
        when(this.roleService.getRoleByName("client")).thenReturn(role);
        when(this.clinicService.addClient(0, "Vasya", "vasya@mail.ru", "2323", role))
                .thenThrow(new ServiceException("Test error"));

        new AddClient(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }
}