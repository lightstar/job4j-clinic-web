package ru.lightstar.clinic.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.pet.Sex;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <code>SetClientPet</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class SetClientPetTest extends Mockito {

    /**
     * Test setting client's pet with correct parameters in servlet.
     */
    @Test
    public void whenDoPostThenResult()
            throws ServletException, IOException, NameException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ClinicService clinicService = mock(ClinicService.class);

        when(request.getParameter("name")).thenReturn("Vasya");
        when(request.getParameter("petType")).thenReturn("cat");
        when(request.getParameter("petName")).thenReturn("Murka");
        when(request.getParameter("petAge")).thenReturn("5");
        when(request.getParameter("petSex")).thenReturn("f");
        when(request.getContextPath()).thenReturn("/context");

        new SetClientPet(clinicService).doPost(request, response);

        verify(clinicService, times(1)).setClientPet("Vasya", "cat",
                "Murka", 5, Sex.F);
        verify(response, atLeastOnce()).sendRedirect("/context/");
    }

    /**
     * Test setting client's pet with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError()
            throws ServletException, IOException, NameException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);

        when(request.getContextPath()).thenReturn("/context");
        when(request.getRequestDispatcher("/WEB-INF/view/SetClientPet.jsp")).thenReturn(dispatcher);

        new SetClientPet(clinicService).doPost(request, response);

        verify(request, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }

    /**
     * Test setting client's pet when clinic service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, NameException, ServiceException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);

        when(request.getParameter("name")).thenReturn("Vasya");
        when(request.getParameter("petType")).thenReturn("cat");
        when(request.getParameter("petName")).thenReturn("Murka");
        when(request.getParameter("petAge")).thenReturn("5");
        when(request.getParameter("petSex")).thenReturn("f");
        when(request.getRequestDispatcher("/WEB-INF/view/SetClientPet.jsp")).thenReturn(dispatcher);
        doThrow(new ServiceException("Test error")).when(clinicService).setClientPet("Vasya",
                "cat", "Murka", 5, Sex.F);

        new SetClientPet(clinicService).doPost(request, response);

        verify(request, atLeastOnce()).setAttribute("error", "Test error.");
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }
}
