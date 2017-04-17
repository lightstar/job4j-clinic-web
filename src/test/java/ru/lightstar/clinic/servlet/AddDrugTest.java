package ru.lightstar.clinic.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <code>AddDrug</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AddDrugTest extends Mockito {

    /**
     * Test adding drug with correct parameters in servlet.
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
        when(request.getContextPath()).thenReturn("/context");
        when(request.getSession()).thenReturn(session);

        new AddDrug(clinicService, drugService).doPost(request, response);

        verify(session, atLeastOnce()).setAttribute(eq("message"), anyString());
        verify(drugService, times(1)).addDrug("aspirin");
        verify(response, atLeastOnce()).sendRedirect("/context/drug");
    }

    /**
     * Test adding drug with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError()
            throws ServletException, IOException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final DrugService drugService = mock(DrugService.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getContextPath()).thenReturn("/context");
        when(request.getSession()).thenReturn(session);

        new AddDrug(clinicService, drugService).doPost(request, response);

        verify(session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(response, atLeastOnce()).sendRedirect("/context/drug");
    }

    /**
     * Test adding drug when drug service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final DrugService drugService = mock(DrugService.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getParameter("name")).thenReturn("aspirin");
        when(request.getContextPath()).thenReturn("/context");
        when(request.getSession()).thenReturn(session);
        when(drugService.addDrug("aspirin"))
                .thenThrow(new ServiceException("Test error"));

        new AddDrug(clinicService, drugService).doPost(request, response);

        verify(session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(response, atLeastOnce()).sendRedirect("/context/drug");
    }
}