package ru.lightstar.clinic.controller.drug;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.lightstar.clinic.controller.ControllerTest;
import ru.lightstar.clinic.drug.Aspirin;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.pet.Cat;
import ru.lightstar.clinic.pet.Pet;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>GiveDrug</code> controller tests.
 *
 * @author Lightstar
 * @since 0.0.1
 */
public class GiveDrugTest extends ControllerTest {

    /**
     * Test correctness of show form request.
     */
    @Test
    public void whenShowFormThenItShows() throws Exception {
        this.mockMvc.perform(get("/drug/give")
                    .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("GiveDrug"))
                .andExpect(forwardedUrl("/WEB-INF/view/GiveDrug.jsp"));
    }

    /**
     * Test correctness of give drug request.
     */
    @Test
    public void whenGiveDrugThenItGives() throws Exception {
        final Pet cat = new Cat("Murka", new DummyOutput());
        final Drug aspirin = new Aspirin();
        when(this.mockClinicService.getClientPet("Vasya")).thenReturn(cat);
        when(this.mockDrugService.takeDrug("aspirin")).thenReturn(aspirin);

        this.mockMvc.perform(post("/drug/give")
                    .with(user("admin").roles("ADMIN"))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "aspirin")
                    .param("clientName", "Vasya"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/drug"))
                .andExpect(redirectedUrl("/drug"))
                .andExpect(flash().attribute("message", is("Gave aspirin (2) to cat 'Murka'.")));

        verify(this.mockClinicService, times(1)).getClientPet("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);

        verify(this.mockDrugService, times(1)).takeDrug("aspirin");
        verifyNoMoreInteractions(this.mockDrugService);
    }

    /**
     * Test correctness of give drug request when <code>ServiceException</code> is thrown.
     */
    @Test
    public void whenGiveDrugWithServiceExceptionThenError() throws Exception {
        when(this.mockClinicService.getClientPet("Vasya")).thenThrow(new ServiceException("Can't get pet"));

        this.mockMvc.perform(post("/drug/give")
                    .with(user("admin").roles("ADMIN"))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "aspirin")
                    .param("clientName", "Vasya"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/drug/give?name=aspirin"))
                .andExpect(redirectedUrl("/drug/give?name=aspirin"))
                .andExpect(flash().attribute("error", is("Can't get pet.")));
    }
}