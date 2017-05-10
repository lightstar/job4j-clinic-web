package ru.lightstar.clinic.controller;

import org.junit.Test;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.pet.Cat;
import ru.lightstar.clinic.pet.Dog;
import ru.lightstar.clinic.pet.Pet;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>ShowPets</code> controller tests.
 *
 * @author Lightstar
 * @since 0.0.1
 */
public class ShowPetsTest extends ControllerTest {

    /**
     * Test correctness of show request.
     */
    @Test
    public void whenShowThenResult() throws Exception {
        final Pet cat = new Cat("Murka", new DummyOutput());
        final Pet dog = new Dog("Bobik", new DummyOutput());

        when(this.mockClinicService.getAllPets()).thenReturn(new Pet[]{cat, dog});

        this.mockMvc.perform(get("/pet"))
                .andExpect(status().isOk())
                .andExpect(view().name("ShowPets"))
                .andExpect(forwardedUrl("/WEB-INF/view/ShowPets.jsp"))
                .andExpect(model().attribute("pets", arrayWithSize(2)))
                .andExpect(model().attribute("pets", arrayContaining(cat, dog)));

        verify(this.mockClinicService, times(1)).getAllPets();
        verifyNoMoreInteractions(this.mockClinicService);
    }
}