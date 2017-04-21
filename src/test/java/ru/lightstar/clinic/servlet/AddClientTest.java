package ru.lightstar.clinic.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.persistence.RoleService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <code>AddClient</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AddClientTest extends Mockito {

    /**
     * Test adding client with correct parameters in servlet.
     */
    @Test
    public void whenDoPostThenResult()
            throws ServletException, IOException, NameException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final RoleService roleService = mock(RoleService.class);
        final HttpSession session = mock(HttpSession.class);
        final Role role = new Role("client");
        role.setId(2);

        when(request.getParameter("pos")).thenReturn("1");
        when(request.getParameter("name")).thenReturn("Vasya");
        when(request.getParameter("email")).thenReturn("vasya@mail.ru");
        when(request.getParameter("phone")).thenReturn("2323");
        when(request.getParameter("role")).thenReturn("client");
        when(roleService.getRoleByName("client")).thenReturn(role);
        when(request.getContextPath()).thenReturn("/context");
        when(request.getSession()).thenReturn(session);

        new AddClient(clinicService, roleService).doPost(request, response);

        verify(session, atLeastOnce()).setAttribute(eq("message"), anyString());
        verify(clinicService, times(1)).addClient(0, "Vasya",
                "vasya@mail.ru", "2323", role);
        verify(response, atLeastOnce()).sendRedirect("/context/");
    }

    /**
     * Test adding client with position not a number in servlet.
     */
    @Test
    public void whenDoPostWithPositionNotANumberThenError()
            throws ServletException, IOException, NameException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final RoleService roleService = mock(RoleService.class);
        final HttpSession session = mock(HttpSession.class);
        final Role role = new Role("client");
        role.setId(2);

        when(request.getParameter("pos")).thenReturn("not-a-number");
        when(request.getParameter("name")).thenReturn("Vasya");
        when(request.getParameter("email")).thenReturn("vasya@mail.ru");
        when(request.getParameter("phone")).thenReturn("2323");
        when(request.getParameter("role")).thenReturn("client");
        when(roleService.getRoleByName("client")).thenReturn(role);

        when(request.getRequestDispatcher("/WEB-INF/view/AddClient.jsp")).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);

        new AddClient(clinicService, roleService).doPost(request, response);

        verify(session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }

    /**
     * Test adding client with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError()
            throws ServletException, IOException, NameException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final RoleService roleService = mock(RoleService.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getRequestDispatcher("/WEB-INF/view/AddClient.jsp")).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);

        new AddClient(clinicService, roleService).doPost(request, response);

        verify(session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }

    /**
     * Test adding client when clinic service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, NameException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final RoleService roleService = mock(RoleService.class);
        final HttpSession session = mock(HttpSession.class);
        final Role role = new Role("client");
        role.setId(2);

        when(request.getParameter("pos")).thenReturn("1");
        when(request.getParameter("name")).thenReturn("Vasya");
        when(request.getParameter("email")).thenReturn("vasya@mail.ru");
        when(request.getParameter("phone")).thenReturn("2323");
        when(request.getParameter("role")).thenReturn("client");
        when(request.getRequestDispatcher("/WEB-INF/view/AddClient.jsp")).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(roleService.getRoleByName("client")).thenReturn(role);
        when(clinicService.addClient(0, "Vasya", "vasya@mail.ru", "2323", role))
                .thenThrow(new ServiceException("Test error"));

        new AddClient(clinicService, roleService).doPost(request, response);

        verify(session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }
}