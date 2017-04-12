package ru.lightstar.clinic.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <code>DeleteClient</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class DeleteClientTest extends Mockito {

    /**
     * Test deleting client with correct parameters in servlet.
     */
    @Test
    public void whenDoPostThenResult()
            throws ServletException, IOException, NameException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ClinicService clinicService = mock(ClinicService.class);

        when(request.getParameter("name")).thenReturn("Vasya");
        when(request.getContextPath()).thenReturn("/context");

        new DeleteClient(clinicService).doPost(request, response);

        verify(clinicService, times(1)).deleteClient("Vasya");
        verify(response, atLeastOnce()).sendRedirect("/context/");
    }

    /**
     * Test deleting client with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError()
            throws ServletException, IOException, NameException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);

        when(request.getContextPath()).thenReturn("/context");
        when(request.getRequestDispatcher("/")).thenReturn(dispatcher);

        new DeleteClient(clinicService).doPost(request, response);

        verify(request, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }

    /**
     * Test deleting client when clinic service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, NameException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);

        when(request.getParameter("name")).thenReturn("Vasya");
        when(request.getRequestDispatcher("/")).thenReturn(dispatcher);
        doThrow(new ServiceException("Test error")).when(clinicService).deleteClient("Vasya");

        new DeleteClient(clinicService).doPost(request, response);

        verify(request, atLeastOnce()).setAttribute("error", "Test error");
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }
}