package ru.lightstar.clinic.controller.drug;

import org.junit.Test;
import ru.lightstar.clinic.controller.ControllerTest;
import ru.lightstar.clinic.drug.Aspirin;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.drug.Glucose;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <code>ShowDrugs</code> controller tests.
 *
 * @author Lightstar
 * @since 0.0.1
 */
public class ShowDrugsTest extends ControllerTest {

    /**
     * Test correctness of show request.
     */
    @Test
    public void whenShowThenResult() throws Exception {
        final Drug aspirin = new Aspirin();
        final Drug glucose = new Glucose();
        final Map<Drug, Integer> drugs = new HashMap<>();
        drugs.put(aspirin, 1);
        drugs.put(glucose, 3);

        when(this.mockDrugService.getAllDrugs()).thenReturn(drugs);

        this.mockMvc.perform(get("/drug")
                    .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("ShowDrugs"))
                .andExpect(forwardedUrl("/WEB-INF/view/ShowDrugs.jsp"))
                .andExpect(model().attribute("drugs", hasEntry(aspirin, 1)))
                .andExpect(model().attribute("drugs", hasEntry(glucose, 3)));

        verify(this.mockDrugService, times(1)).getAllDrugs();
        verifyNoMoreInteractions(this.mockDrugService);
    }
}