package ru.lightstar.clinic.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <code>GiveDrugTest</code> class.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class GiveDrugTest extends Mockito {

    /**
     * Test giving drug with correct parameters in servlet.
     */
    @Test
    public void whenDoPostThenResult()
            throws ServletException, IOException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final DrugService drugService = mock(DrugService.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getParameter("name")).thenReturn("aspirin");
        when(request.getParameter("clientName")).thenReturn("Vasya");
        when(request.getContextPath()).thenReturn("/context");
        when(request.getSession()).thenReturn(session);

        new GiveDrug(clinicService, drugService).doPost(request, response);

        verify(session, atLeastOnce()).setAttribute(eq("message"), anyString());
        verify(clinicService, times(1)).getClientPet("Vasya");
        verify(drugService, times(1)).takeDrug("aspirin");
        verify(response, atLeastOnce()).sendRedirect("/context/drug");
    }

    /**
     * Test giving drug with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError()
            throws ServletException, IOException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final DrugService drugService = mock(DrugService.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getRequestDispatcher("/WEB-INF/view/GiveDrug.jsp")).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);

        new GiveDrug(clinicService, drugService).doPost(request, response);

        verify(session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }

    /**
     * Test giving drug when drug service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final DrugService drugService = mock(DrugService.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getParameter("name")).thenReturn("aspirin");
        when(request.getParameter("clientName")).thenReturn("Vasya");
        when(request.getRequestDispatcher("/WEB-INF/view/GiveDrug.jsp")).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(drugService.takeDrug("aspirin")).thenThrow(new ServiceException("Test error"));

        new GiveDrug(clinicService, drugService).doPost(request, response);

        verify(session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }

    /**
     * Test giving drug when client can't be found or doesn't have pet.
     */
    @Test
    public void whenDoPostWithWrongClientThenError()
            throws ServletException, IOException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final DrugService drugService = mock(DrugService.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getParameter("name")).thenReturn("aspirin");
        when(request.getParameter("clientName")).thenReturn("Vasya");
        when(request.getRequestDispatcher("/WEB-INF/view/GiveDrug.jsp")).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(clinicService.getClientPet("Vasya")).thenThrow(new ServiceException("Test error"));

        new GiveDrug(clinicService, drugService).doPost(request, response);

        verify(drugService, never()).takeDrug(anyString());
        verify(session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }
}