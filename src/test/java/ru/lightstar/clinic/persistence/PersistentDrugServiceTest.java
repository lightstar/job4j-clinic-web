package ru.lightstar.clinic.persistence;

import org.mockito.Mockito;
import ru.lightstar.clinic.drug.Aspirin;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.drug.Glucose;
import ru.lightstar.clinic.exception.ServiceException;

import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Base class for testing <code>PersistentDrugService</code> subclasses.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class PersistentDrugServiceTest extends Mockito {

    /**
     * Size of clinic used in tests.
     */
    protected static final int CLINIC_SIZE = 10;

    /**
     * <code>PersistentDrugService</code> object used in all tests. Must be set in subclasses.
     */
    protected PersistentDrugService drugService;

    /**
     * Test correctness of <code>loadDrugs</code> method.
     */
    public void whenLoadDrugsThenItLoads() throws ServiceException {
        final Map<Drug, Integer> drugs = this.drugService.getAllDrugs();
        assertThat(drugs.size(), is(2));
        assertThat(drugs.get(new Aspirin()), is(3));
        assertThat(drugs.get(new Glucose()), is(1));
    }

    /**
     * Test correctness of <code>addDrug</code> method.
     */
    public void whenAddDrugThenItAdds() throws ServiceException, SQLException {
        this.drugService.addDrug("aspirin");

        final Map<Drug, Integer> drugs = this.drugService.getAllDrugs();
        assertThat(drugs.size(), is(2));
        assertThat(drugs.get(new Aspirin()), is(4));
    }

    /**
     * Test correctness of <code>addDrug</code> method when exception is thrown.
     */
    public void whenAddDrugWithExceptionThenItDoNotAdds() throws ServiceException, SQLException {
        Throwable expectedException = null;
        try {
            this.drugService.addDrug("aspirin");
        } catch (ServiceException e) {
            expectedException = e;
        }

        assertThat(expectedException, instanceOf(ServiceException.class));

        final Map<Drug, Integer> drugs = this.drugService.getAllDrugs();
        assertThat(drugs.size(), is(2));
        assertThat(drugs.get(new Aspirin()), is(3));
    }

    /**
     * Test correctness of <code>takeDrug</code> method.
     */
    public void whenTakeDrugThenItDeletes() throws ServiceException, SQLException {
        this.drugService.takeDrug("aspirin");

        final Map<Drug, Integer> drugs = this.drugService.getAllDrugs();
        assertThat(drugs.size(), is(2));
        assertThat(drugs.get(new Aspirin()), is(2));
    }

    /**
     * Test correctness of <code>takeDrug</code> method when exception is thrown.
     */
    public void whenTakeDrugWithExceptionThenItDoNotDeletes() throws ServiceException, SQLException {
        Throwable expectedException = null;
        try {
            this.drugService.takeDrug("aspirin");
        } catch (ServiceException e) {
            expectedException = e;
        }

        assertThat(expectedException, instanceOf(ServiceException.class));

        final Map<Drug, Integer> drugs = this.drugService.getAllDrugs();
        assertThat(drugs.size(), is(2));
        assertThat(drugs.get(new Aspirin()), is(3));
    }
}
