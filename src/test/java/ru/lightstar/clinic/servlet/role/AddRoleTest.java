package ru.lightstar.clinic.servlet.role;

import org.junit.Test;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.servlet.ServletTest;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * <code>AddRoleTest</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AddRoleTest extends ServletTest {

    /**
     * Test correctness of <code>doPost</code> method.
     */
    @Test
    public void whenDoPostThenResult() throws ServiceException, ServletException, IOException {
        when(this.request.getParameter("name")).thenReturn("client");

        new AddRole(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("message"), anyString());
        verify(this.roleService, times(1)).addRole("client");
        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/role");
    }

    /**
     * Test correctness of <code>doPost</code> method with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError() throws ServiceException, ServletException, IOException {
        new AddRole(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/role");
    }

    /**
     * Test adding role when role service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("client");
        doThrow(new ServiceException("Test error")).when(this.roleService).addRole("client");

        new AddRole(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/role");
    }
}