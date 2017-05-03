package ru.lightstar.clinic.servlet;

import org.junit.Test;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.pet.Cat;
import ru.lightstar.clinic.pet.Sex;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * <code>UpdateClientPet</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class UpdateClientPetTest extends ServletTest {

    /**
     * Test correctness of <code>doGet</code> method.
     */
    @Test
    public void whenDoGetThenResult() throws ServletException, IOException, ServiceException {
        when(this.request.getRequestDispatcher("/WEB-INF/view/UpdateClientPet.jsp")).thenReturn(this.dispatcher);
        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.clinicService.getClientPet("Vasya")).thenReturn(new Cat("Murka", new DummyOutput()));

        new UpdateClientPet(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test correctness of <code>doGet</code> method when client name is absent.
     */
    @Test
    public void whenDoGetWithAbsentClientNameThenRedirect() throws ServletException, IOException, ServiceException {
        when(this.request.getRequestDispatcher("/WEB-INF/view/UpdateClientPet.jsp")).thenReturn(this.dispatcher);

        new UpdateClientPet(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.response, atLeastOnce()).sendRedirect("/context/");
    }

    /**
     * Test correctness of <code>doGet</code> method when pet not found.
     */
    @Test
    public void whenDoGetWithPetNotFoundThenRedirect() throws ServletException, IOException, ServiceException {
        when(this.request.getRequestDispatcher("/WEB-INF/view/UpdateClientPet.jsp")).thenReturn(this.dispatcher);
        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.clinicService.getClientPet("Vasya")).thenThrow(new ServiceException("Pet not found"));

        new UpdateClientPet(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.response, atLeastOnce()).sendRedirect("/context/");
    }

    /**
     * Test updating client's pet with correct parameters in servlet.
     */
    @Test
    public void whenDoPostThenResult()
            throws ServletException, IOException, NameException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getParameter("newName")).thenReturn("Murka");
        when(this.request.getParameter("newAge")).thenReturn("5");
        when(this.request.getParameter("newSex")).thenReturn("f");

        new UpdateClientPet(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("message"), anyString());
        verify(this.clinicService, times(1)).updateClientPet("Vasya",
                "Murka", 5, Sex.F);
        verify(this.response, atLeastOnce()).sendRedirect("/context/servlets/");
    }

    /**
     * Test updating client's pet with null parameters in servlet.
     */
    @Test
    public void whenDoPostWithNullParametersThenError()
            throws ServletException, IOException, NameException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getRequestDispatcher("/WEB-INF/view/UpdateClientPet.jsp")).thenReturn(this.dispatcher);
        when(this.clinicService.getClientPet("Vasya")).thenReturn(new Cat("Murka", new DummyOutput()));

        new UpdateClientPet(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute(eq("error"), anyString());
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }

    /**
     * Test updating client's pet when clinic service throws exception.
     */
    @Test
    public void whenDoPostWithServiceExceptionThenError()
            throws ServletException, IOException, NameException, ServiceException {
        when(this.request.getParameter("name")).thenReturn("Vasya");
        when(this.request.getParameter("newName")).thenReturn("Murka");
        when(this.request.getParameter("newAge")).thenReturn("5");
        when(this.request.getParameter("newSex")).thenReturn("f");
        when(this.request.getRequestDispatcher("/WEB-INF/view/UpdateClientPet.jsp")).thenReturn(this.dispatcher);
        doThrow(new ServiceException("Test error")).when(this.clinicService).updateClientPet("Vasya",
                "Murka", 5, Sex.F);
        when(this.clinicService.getClientPet("Vasya")).thenReturn(new Cat("Murka", new DummyOutput()));

        new UpdateClientPet(this.clinicService, this.roleService).doPost(this.request, this.response);

        verify(this.session, atLeastOnce()).setAttribute("error", "Test error.");
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }
}