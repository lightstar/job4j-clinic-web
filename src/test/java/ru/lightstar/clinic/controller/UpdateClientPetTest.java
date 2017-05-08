package ru.lightstar.clinic.controller;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.pet.Cat;
import ru.lightstar.clinic.pet.Pet;
import ru.lightstar.clinic.pet.Sex;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>UpdateClientPet</code> controller tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class UpdateClientPetTest extends ControllerTest {

    /**
     * Test correctness of show form request.
     */
    @Test
    public void whenShowFormThenResult() throws Exception {
        final Pet vasyaCat = new Cat("Murka", new DummyOutput());
        vasyaCat.setAge(5);
        vasyaCat.setSex(Sex.F);
        when(this.mockClinicService.getClientPet("Vasya")).thenReturn(vasyaCat);

        this.mockMvc.perform(get("/client/pet/update")
                    .param("name", "Vasya"))
                .andExpect(status().isOk())
                .andExpect(view().name("UpdateClientPet"))
                .andExpect(forwardedUrl("/WEB-INF/view/UpdateClientPet.jsp"))
                .andExpect(model().attribute("newName", is("Murka")))
                .andExpect(model().attribute("newAge", is(5)))
                .andExpect(model().attribute("newSex", is("f")));

        verify(this.mockClinicService, times(1)).getClientPet("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
    }

    /**
     * Test correctness of show form request when pet not found.
     */
    @Test
    public void whenShowAndPetNotFoundThenGoHome() throws Exception {
        when(this.mockClinicService.getClientPet("Vasya")).thenThrow(new ServiceException("Pet not found"));

        this.mockMvc.perform(get("/client/pet/update")
                    .param("name", "Vasya"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"));

        verify(this.mockClinicService, times(1)).getClientPet("Vasya");
        verifyNoMoreInteractions(this.mockClinicService);
    }

    /**
     * Test correctness of update client pet request.
     */
    @Test
    public void whenUpdateClientPetThenItUpdates() throws Exception {
        this.mockMvc.perform(post("/client/pet/update")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vasya")
                    .param("newName", "Barsik")
                    .param("newAge", "6")
                    .param("newSex", "m"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("message", is("Pet updated.")));

        verify(this.mockClinicService, times(1)).updateClientPet("Vasya","Barsik",
                6, Sex.M);
        verifyNoMoreInteractions(this.mockClinicService);
    }

    /**
     * Test correctness of update client pet request when <code>ServiceException</code> is thrown.
     */
    @Test
    public void whenUpdateClientPetWithServiceExceptionThenError() throws Exception {
        doThrow(new ServiceException("Can't update pet")).when(this.mockClinicService)
                .updateClientPet("Vasya", "Barsik", 6, Sex.M);

        this.mockMvc.perform(post("/client/pet/update")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vasya")
                    .param("newName", "Barsik")
                    .param("newAge", "6")
                    .param("newSex", "m"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/client/pet/update?name=Vasya"))
                .andExpect(redirectedUrl("/client/pet/update?name=Vasya"))
                .andExpect(flash().attribute("error", is("Can't update pet.")));
    }
}