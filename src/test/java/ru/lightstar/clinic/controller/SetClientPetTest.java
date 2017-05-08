package ru.lightstar.clinic.controller;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.pet.Sex;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>SetClientPet</code> controller tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class SetClientPetTest extends ControllerTest {

    /**
     * Test correctness of show form request.
     */
    @Test
    public void whenShowFormThenItShows() throws Exception {
        this.mockMvc.perform(get("/client/pet/set"))
                .andExpect(status().isOk())
                .andExpect(view().name("SetClientPet"))
                .andExpect(forwardedUrl("/WEB-INF/view/SetClientPet.jsp"));
    }

    /**
     * Test correctness of set client pet request.
     */
    @Test
    public void whenSetClientPetThenItSets() throws Exception {
        this.mockMvc.perform(post("/client/pet/set")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vova")
                    .param("petType", "dog")
                    .param("petName", "Bobik")
                    .param("petAge", "5")
                    .param("petSex", "m"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("message", is("Pet was set.")));

        verify(this.mockClinicService, times(1)).setClientPet("Vova",
                "dog", "Bobik", 5, Sex.M);
        verifyNoMoreInteractions(this.mockClinicService);
    }

    /**
     * Test correctness of set client pet request when <code>ServiceException</code> is thrown.
     */
    @Test
    public void whenAddClientWithServiceExceptionThenError() throws Exception {
        when(this.mockClinicService.setClientPet("Vova", "dog", "Bobik", 5, Sex.M))
                .thenThrow(new ServiceException("Can't set pet"));

        this.mockMvc.perform(post("/client/pet/set")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "Vova")
                    .param("petType", "dog")
                    .param("petName", "Bobik")
                    .param("petAge", "5")
                    .param("petSex", "m"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/client/pet/set?name=Vova"))
                .andExpect(redirectedUrl("/client/pet/set?name=Vova"))
                .andExpect(flash().attribute("error", is("Can't set pet.")));
    }
}