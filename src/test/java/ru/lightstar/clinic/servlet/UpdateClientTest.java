package ru.lightstar.clinic.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.pet.Pet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <code>UpdateClient</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class UpdateClientTest extends Mockito {

    /**
     * Test updating client with correct parameters in servlet.
     */
    @Test
    public void whenDoPostThenResult()
            throws ServletException, IOException, NameException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getParameter("name")).thenReturn("Vasya");
        when(request.getParameter("newName")).thenReturn("Vova");
        when(request.getParameter("newEmail")).thenReturn("vova@mail.ru");
        when(request.getParameter("newPhone")).thenReturn("22222");
        when(request.getContextPath()).thenReturn("/context");
        when(request.getSession()).thenReturn(session);

        new UpdateClient(clinicService).doPost(request, response);

        verify(session, atLeastOnce()).setAttribute(eq("message"), anyString());
        verify(clinicService, times(1)).updateClient("Vasya", "Vova",
                "vova@mail.ru", "22222");
        verify(response, atLeastOnce()).sendRedirect("/context/");
    }

    /**
     * Test updating client with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError()
            throws ServletException, IOException, NameException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getParameter("name")).thenReturn("Vasya");
        when(request.getContextPath()).thenReturn("/context");
        when(request.getRequestDispatcher("/WEB-INF/view/UpdateClient.jsp")).thenReturn(dispatcher);
        when(clinicService.findClientByName("Vasya")).thenReturn(new Client("Vasya", Pet.NONE, 0));
        when(request.getSession()).thenReturn(session);

        new UpdateClient(clinicService).doPost(request, response);

        verify(session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }

    /**
     * Test updating client when clinic service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, NameException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getParameter("name")).thenReturn("Vasya");
        when(request.getParameter("newName")).thenReturn("Vova");
        when(request.getParameter("newEmail")).thenReturn("vova@mail.ru");
        when(request.getParameter("newPhone")).thenReturn("22222");
        when(request.getRequestDispatcher("/WEB-INF/view/UpdateClient.jsp")).thenReturn(dispatcher);
        doThrow(new ServiceException("Test error")).when(clinicService).updateClient("Vasya", "Vova",
                "vova@mail.ru", "22222");
        when(clinicService.findClientByName("Vasya")).thenReturn(new Client("Vasya", Pet.NONE, 0));
        when(request.getSession()).thenReturn(session);

        new UpdateClient(clinicService).doPost(request, response);

        verify(session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }
}