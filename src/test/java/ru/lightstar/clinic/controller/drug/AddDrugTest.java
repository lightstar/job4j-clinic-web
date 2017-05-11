package ru.lightstar.clinic.controller.drug;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.lightstar.clinic.controller.ControllerTest;
import ru.lightstar.clinic.exception.ServiceException;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>AddDrug</code> controller tests.
 *
 * @author Lightstar
 * @since 0.0.1
 */
public class AddDrugTest extends ControllerTest {

    /**
     * Test correctness of add drug request.
     */
    @Test
    public void whenAddDrugThenItAdds() throws Exception {
        this.mockMvc.perform(post("/drug/add")
                    .with(user("admin").roles("ADMIN"))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "aspirin"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/drug"))
                .andExpect(redirectedUrl("/drug"))
                .andExpect(flash().attribute("message", is("Drug added.")));

        verify(this.mockDrugService, times(1)).addDrug("aspirin");
        verifyNoMoreInteractions(this.mockDrugService);
    }

    /**
     * Test correctness of add drug request when <code>ServiceException</code> is thrown.
     */
    @Test
    public void whenAddDrugWithServiceExceptionThenError() throws Exception {
        doThrow(new ServiceException("Can't add drug")).when(this.mockDrugService).addDrug("aspirin");

        this.mockMvc.perform(post("/drug/add")
                    .with(user("admin").roles("ADMIN"))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "aspirin"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/drug"))
                .andExpect(redirectedUrl("/drug"))
                .andExpect(flash().attribute("error", is("Can't add drug.")));
    }
}