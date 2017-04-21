package ru.lightstar.clinic.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.persistence.RoleService;
import ru.lightstar.clinic.pet.Cat;
import ru.lightstar.clinic.pet.Pet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <code>ShowPets</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowPetsTest extends Mockito {

    /**
     * Test correctness of <code>doGet</code> method.
     */
    @Test
    public void whenDoGetWithoutFiltersThenResult() throws ServletException, IOException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final RoleService roleService = mock(RoleService.class);
        final Pet[] pets = new Pet[]{new Cat("Murka", new DummyOutput())};

        when(request.getRequestDispatcher("/WEB-INF/view/ShowPets.jsp")).thenReturn(dispatcher);
        when(clinicService.getAllPets()).thenReturn(pets);

        new ShowPets(clinicService, roleService).doGet(request, response);

        verify(clinicService, atLeastOnce()).getAllPets();
        verify(request, atLeastOnce()).setAttribute("pets", pets);
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }
}