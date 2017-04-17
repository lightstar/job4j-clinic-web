package ru.lightstar.clinic.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.drug.Aspirin;
import ru.lightstar.clinic.drug.Drug;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * <code>ShowDrugs</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowDrugsTest extends Mockito {

    /**
     * Test correctness of <code>doGet</code> method.
     */
    @Test
    public void whenDoGetWithoutFiltersThenResult() throws ServletException, IOException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        final ClinicService clinicService = mock(ClinicService.class);
        final DrugService drugService = mock(DrugService.class);
        final Map<Drug,Integer> drugs = Collections.singletonMap(new Aspirin(), 1);

        when(request.getRequestDispatcher("/WEB-INF/view/ShowDrugs.jsp")).thenReturn(dispatcher);
        when(drugService.getAllDrugs()).thenReturn(drugs);

        new ShowDrugs(clinicService, drugService).doGet(request, response);

        verify(drugService, atLeastOnce()).getAllDrugs();
        verify(request, atLeastOnce()).setAttribute("drugs", drugs);
        verify(dispatcher, atLeastOnce()).forward(request, response);
    }
}