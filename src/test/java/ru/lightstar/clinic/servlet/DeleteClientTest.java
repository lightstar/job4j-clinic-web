package ru.lightstar.clinic.servlet;

import org.junit.Test;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * <code>DeleteClient</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class DeleteClientTest extends ServletTest {

    /**
     * Test deleting client with correct parameters in servlet.
     */
    @Test
    public void whenDoPostThenResult()
            throws ServletException, IOException, NameException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("Vasya");

        new DeleteClient(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("message"), anyString());
        verify(this.clinicService, times(1)).deleteClient("Vasya");
        verify(this.response, atLeastOnce()).sendRedirect("/context/");
    }

    /**
     * Test deleting client with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError()
            throws ServletException, IOException, NameException, ServiceException {
        new DeleteClient(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(this.response, atLeastOnce()).sendRedirect("/context/");
    }

    /**
     * Test deleting client when clinic service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, NameException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("Vasya");
        doThrow(new ServiceException("Test error")).when(this.clinicService).deleteClient("Vasya");

        new DeleteClient(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(this.response, atLeastOnce()).sendRedirect("/context/");
    }
}