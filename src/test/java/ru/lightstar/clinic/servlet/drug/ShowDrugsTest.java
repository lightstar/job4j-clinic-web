package ru.lightstar.clinic.servlet.drug;

import org.junit.Test;
import ru.lightstar.clinic.drug.Aspirin;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.servlet.ServletTest;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * <code>ShowDrugs</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowDrugsTest extends ServletTest {

    /**
     * Test correctness of <code>doGet</code> method.
     */
    @Test
    public void whenDoGetThenResult() throws ServletException, IOException {
        final Map<Drug,Integer> drugs = Collections.singletonMap(new Aspirin(), 1);

        when(this.request.getRequestDispatcher("/WEB-INF/view/ShowDrugs.jsp")).thenReturn(this.dispatcher);
        when(this.drugService.getAllDrugs()).thenReturn(drugs);

        new ShowDrugs(this.clinicService, this.roleService, this.drugService).doGet(this.request, this.response);

        verify(this.drugService, atLeastOnce()).getAllDrugs();
        verify(this.request, atLeastOnce()).setAttribute("drugs", drugs);
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }
}