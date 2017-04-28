package ru.lightstar.clinic.servlet.drug;

import org.junit.Test;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.servlet.ServletTest;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * <code>GiveDrugTest</code> class.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class GiveDrugTest extends ServletTest {

    /**
     * Test correctness of <code>doGet</code> method.
     */
    @Test
    public void whenDoGetThenResult() throws ServletException, IOException, ServiceException {
        when(this.request.getRequestDispatcher("/WEB-INF/view/GiveDrug.jsp")).thenReturn(this.dispatcher);

        new GiveDrug(this.clinicService, this.roleService, this.drugService).doGet(this.request, this.response);

        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test giving drug with correct parameters in servlet.
     */
    @Test
    public void whenDoPostThenResult()
            throws ServletException, IOException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("aspirin");
        when(this.request.getParameter("clientName")).thenReturn("Vasya");

        new GiveDrug(this.clinicService, this.roleService, this.drugService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("message"), anyString());
        verify(this.clinicService, times(1)).getClientPet("Vasya");
        verify(this.drugService, times(1)).takeDrug("aspirin");
        verify(this.response, atLeastOnce()).sendRedirect("/context/drug");
    }

    /**
     * Test giving drug with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError()
            throws ServletException, IOException, ServiceException {
        when(this.request.getRequestDispatcher("/WEB-INF/view/GiveDrug.jsp")).thenReturn(this.dispatcher);

        new GiveDrug(this.clinicService, this.roleService, this.drugService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test giving drug when drug service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("aspirin");
        when(this.request.getParameter("clientName")).thenReturn("Vasya");
        when(this.request.getRequestDispatcher("/WEB-INF/view/GiveDrug.jsp")).thenReturn(this.dispatcher);
        when(this.drugService.takeDrug("aspirin")).thenThrow(new ServiceException("Test error"));

        new GiveDrug(this.clinicService, this.roleService, this.drugService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test giving drug when client can't be found or doesn't have pet.
     */
    @Test
    public void whenDoPostWithWrongClientThenError()
            throws ServletException, IOException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("aspirin");
        when(this.request.getParameter("clientName")).thenReturn("Vasya");
        when(this.request.getRequestDispatcher("/WEB-INF/view/GiveDrug.jsp")).thenReturn(this.dispatcher);
        when(this.clinicService.getClientPet("Vasya")).thenThrow(new ServiceException("Test error"));

        new GiveDrug(this.clinicService, this.roleService, this.drugService).doPost(this.request, this.response);

        verify(this.drugService, never()).takeDrug(anyString());
        verify(this.session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }
}