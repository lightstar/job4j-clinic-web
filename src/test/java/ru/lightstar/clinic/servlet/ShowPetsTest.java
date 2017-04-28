package ru.lightstar.clinic.servlet;

import org.junit.Test;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.pet.Cat;
import ru.lightstar.clinic.pet.Pet;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * <code>ShowPets</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowPetsTest extends ServletTest {

    /**
     * Test correctness of <code>doGet</code> method.
     */
    @Test
    public void whenDoGetWithoutFiltersThenResult() throws ServletException, IOException {
        final Pet[] pets = new Pet[]{new Cat("Murka", new DummyOutput())};

        when(this.request.getRequestDispatcher("/WEB-INF/view/ShowPets.jsp")).thenReturn(this.dispatcher);
        when(this.clinicService.getAllPets()).thenReturn(pets);

        new ShowPets(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.clinicService, atLeastOnce()).getAllPets();
        verify(this.request, atLeastOnce()).setAttribute("pets", pets);
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }
}