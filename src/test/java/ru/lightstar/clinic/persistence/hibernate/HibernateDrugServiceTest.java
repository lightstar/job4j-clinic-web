package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.SessionFactory;
import org.junit.Test;
import ru.lightstar.clinic.Clinic;
import ru.lightstar.clinic.SessionFactoryMocker;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.PersistentDrugServiceTest;

import javax.persistence.PersistenceException;
import java.sql.SQLException;

/**
 * <code>HibernateDrugService</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class HibernateDrugServiceTest extends PersistentDrugServiceTest {

    /**
     * Helper object used to mock hibernate session factory.
     */
    private final SessionFactoryMocker hibernateMocker;

    /**
     * Constructs <code>HibernateDrugServiceTest</code> object.
     */
    public HibernateDrugServiceTest() {
        super();
        this.hibernateMocker = new SessionFactoryMocker();
        final SessionFactory sessionFactory = this.hibernateMocker.getSessionFactory();
        this.drugService = new HibernateDrugService(new Clinic(CLINIC_SIZE), sessionFactory);
    }


    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenLoadDrugsThenItLoads() throws ServiceException {
        super.whenLoadDrugsThenItLoads();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenAddDrugThenItAdds() throws ServiceException, SQLException {
        super.whenAddDrugThenItAdds();

        verify(this.hibernateMocker.getSession(), atLeastOnce())
                .beginTransaction();
        verify(this.hibernateMocker.getSession(), times(1))
                .save(any(Drug.class));
        verify(this.hibernateMocker.getTransaction(), atLeastOnce())
                .commit();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenAddDrugWithExceptionThenItDoNotAdds() throws ServiceException, SQLException {
        doThrow(PersistenceException.class).when(this.hibernateMocker.getSession()).save(any(Drug.class));
        super.whenAddDrugWithExceptionThenItDoNotAdds();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenTakeDrugThenItDeletes() throws ServiceException, SQLException {
        super.whenTakeDrugThenItDeletes();

        verify(this.hibernateMocker.getSession(), atLeastOnce())
                .beginTransaction();
        verify(this.hibernateMocker.getSession(), times(1))
                .delete(any(Drug.class));
        verify(this.hibernateMocker.getTransaction(), atLeastOnce())
                .commit();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenTakeDrugWithExceptionThenItDoNotDeletes() throws ServiceException, SQLException {
        doThrow(PersistenceException.class).when(this.hibernateMocker.getSession()).delete(any(Drug.class));
        super.whenTakeDrugWithExceptionThenItDoNotDeletes();
    }
}