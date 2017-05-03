package ru.lightstar.clinic.servlet;

import org.junit.Test;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.pet.Sex;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * <code>SetClientPet</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class SetClientPetTest extends ServletTest {

    /**
     * Test correctness of <code>doGet</code> method.
     */
    @Test
    public void whenDoGetThenResult() throws ServletException, IOException, ServiceException {
        when(this.request.getRequestDispatcher("/WEB-INF/view/SetClientPet.jsp")).thenReturn(this.dispatcher);

        new SetClientPet(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test setting client's pet with correct parameters in servlet.
     */
    @Test
    public void whenDoPostThenResult()
            throws ServletException, IOException, NameException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getParameter("petType")).thenReturn("cat");
        when(this.request.getParameter("petName")).thenReturn("Murka");
        when(this.request.getParameter("petAge")).thenReturn("5");
        when(this.request.getParameter("petSex")).thenReturn("f");

        new SetClientPet(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("message"), anyString());
        verify(this.clinicService, times(1)).setClientPet("Vasya", "cat",
                "Murka", 5, Sex.F);
        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/");
    }

    /**
     * Test setting client's pet with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError()
            throws ServletException, IOException, NameException, ServiceException {
        when(this.request.getRequestDispatcher("/WEB-INF/view/SetClientPet.jsp")).thenReturn(this.dispatcher);

        new SetClientPet(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test setting client's pet when clinic service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, NameException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getParameter("petType")).thenReturn("cat");
        when(this.request.getParameter("petName")).thenReturn("Murka");
        when(this.request.getParameter("petAge")).thenReturn("5");
        when(this.request.getParameter("petSex")).thenReturn("f");
        when(this.request.getRequestDispatcher("/WEB-INF/view/SetClientPet.jsp")).thenReturn(this.dispatcher);
        doThrow(new ServiceException("Test error")).when(this.clinicService).setClientPet("Vasya",
                "cat", "Murka", 5, Sex.F);

        new SetClientPet(this.clinicService, roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }
}
