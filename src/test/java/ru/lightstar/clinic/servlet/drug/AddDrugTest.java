package ru.lightstar.clinic.servlet.drug;

import org.junit.Test;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.servlet.ServletTest;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * <code>AddDrug</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AddDrugTest extends ServletTest {

    /**
     * Test adding drug with correct parameters in servlet.
     */
    @Test
    public void whenDoPostThenResult()
            throws ServletException, IOException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("aspirin");

        new AddDrug(this.clinicService, this.roleService, this.drugService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("message"), anyString());
        verify(this.drugService, times(1)).addDrug("aspirin");
        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/drug");
    }

    /**
     * Test adding drug with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError()
            throws ServletException, IOException, ServiceException {
        new AddDrug(this.clinicService, this.roleService, this.drugService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/drug");
    }

    /**
     * Test adding drug when drug service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("aspirin");
        when(this.drugService.addDrug("aspirin"))
                .thenThrow(new ServiceException("Test error"));

        new AddDrug(this.clinicService, this.roleService, this.drugService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/drug");
    }
}