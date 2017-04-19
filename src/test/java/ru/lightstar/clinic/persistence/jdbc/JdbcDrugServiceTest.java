package ru.lightstar.clinic.persistence.jdbc;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.Clinic;
import ru.lightstar.clinic.JdbcConnectionMocker;
import ru.lightstar.clinic.drug.Aspirin;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.drug.Glucose;
import ru.lightstar.clinic.exception.ServiceException;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * <code>JdbcDrugService</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcDrugServiceTest extends Mockito {

    /**
     * Size of clinic used in tests.
     */
    private static final int CLINIC_SIZE = 10;

    /**
     * Helper object used to mock jdbc connection.
     */
    private final JdbcConnectionMocker jdbcMocker;

    /**
     * <code>JdbcDrugService</code> object used in all tests.
     */
    private final JdbcDrugService drugService;

    /**
     * Constructs <code>JdbcDrugServiceTest</code> object.
     */
    public JdbcDrugServiceTest() {
        final ServletContext context = mock(ServletContext.class);
        this.jdbcMocker = new JdbcConnectionMocker();
        final Connection connection = jdbcMocker.getConnection();
        when(context.getAttribute("jdbcConnection")).thenReturn(connection);
        this.drugService = new JdbcDrugService(new Clinic(CLINIC_SIZE), context);
    }

    /**
     * Test correctness of <code>loadDrugs</code> method.
     */
    @Test
    public void whenLoadDrugsThenItLoads() throws ServiceException {
        this.drugService.loadDrugs();

        final Map<Drug, Integer> drugs = this.drugService.getAllDrugs();

        assertThat(drugs.size(), is(2));
        assertThat(drugs.get(new Aspirin()), is(3));
        assertThat(drugs.get(new Glucose()), is(1));
    }

    /**
     * Test correctness of <code>addDrug</code> method.
     */
    @Test
    public void whenAddDrugThenItAdds() throws ServiceException, SQLException {
        this.drugService.addDrug("aspirin");

        verify(this.jdbcMocker.getAddDrugStatement(), times(1))
                .setString(1, "aspirin");
        verify(this.jdbcMocker.getAddDrugStatement(), times(1))
                .setInt(2, 2);
        verify(this.jdbcMocker.getAddDrugStatement(), times(1))
                .executeUpdate();
        verify(this.jdbcMocker.getGeneratedKeyForAddDrugResultSet(), times(1))
                .getInt(1);

        final Map<Drug, Integer> drugs = this.drugService.getAllDrugs();

        assertThat(drugs.size(), is(1));
        assertThat(drugs.get(new Aspirin()), is(1));
    }

    /**
     * Test correctness of <code>addDrug</code> method when <code>SQLException</code> is thrown.
     */
    @Test
    public void whenAddDrugWithSQLExceptionThenItDoNotAdds() throws ServiceException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getAddDrugStatement()).executeUpdate();

        Throwable expectedException = null;
        try {
            this.drugService.addDrug("aspirin");
        } catch (ServiceException e) {
            expectedException = e;
        }

        assertThat(expectedException, instanceOf(ServiceException.class));

        final Map<Drug, Integer> drugs = this.drugService.getAllDrugs();
        assertThat(drugs.size(), is(0));
    }

    /**
     * Test correctness of <code>takeDrug</code> method.
     */
    @Test
    public void whenTakeDrugThenItDeletes() throws ServiceException, SQLException {
        this.drugService.addDrug("aspirin");
        this.drugService.addDrug("glucose");
        this.drugService.takeDrug("aspirin");

        verify(this.jdbcMocker.getDeleteDrugStatement(), times(1))
                .setInt(1, 6);
        verify(this.jdbcMocker.getDeleteDrugStatement(), times(1))
                .executeUpdate();

        final Map<Drug, Integer> drugs = this.drugService.getAllDrugs();

        assertThat(drugs.size(), is(1));
        assertThat(drugs.containsKey(new Aspirin()), is(false));
        assertThat(drugs.containsKey(new Glucose()), is(true));
    }

    /**
     * Test correctness of <code>takeDrug</code> method when <code>SQLException</code> is thrown.
     */
    @Test
    public void whenTakeDrugWithSQLExceptionThenItDoNotDeletes() throws ServiceException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getDeleteDrugStatement()).executeUpdate();

        Throwable expectedException = null;
        try {
            this.drugService.addDrug("aspirin");
            this.drugService.addDrug("glucose");
            this.drugService.takeDrug("aspirin");
        } catch (ServiceException e) {
            expectedException = e;
        }

        assertThat(expectedException, instanceOf(ServiceException.class));

        final Map<Drug, Integer> drugs = this.drugService.getAllDrugs();

        assertThat(drugs.size(), is(2));
        assertThat(drugs.containsKey(new Aspirin()), is(true));
        assertThat(drugs.containsKey(new Glucose()), is(true));
    }
}