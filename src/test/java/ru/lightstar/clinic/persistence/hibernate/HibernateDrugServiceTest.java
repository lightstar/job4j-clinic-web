package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.hibernate5.HibernateTemplate;
import ru.lightstar.clinic.Clinic;
import ru.lightstar.clinic.SessionFactoryMocker;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.PersistentDrugServiceTest;

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
    private SessionFactoryMocker hibernateMocker;

    /**
     * Initialize test.
     */
    @Before
    public void initTest() {
        this.hibernateMocker = new SessionFactoryMocker();
        final SessionFactory sessionFactory = this.hibernateMocker.getSessionFactory();
        final HibernateTemplate hibernateTemplate = new HibernateTemplate();
        hibernateTemplate.setSessionFactory(sessionFactory);
        this.drugService = new HibernateDrugService(new Clinic(CLINIC_SIZE), hibernateTemplate);
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
        verify(this.hibernateMocker.getSession(), times(1))
                .save(any(Drug.class));
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenAddDrugWithExceptionThenItDoNotAdds() throws ServiceException, SQLException {
        doThrow(HibernateException.class).when(this.hibernateMocker.getSession()).save(any(Drug.class));
        super.whenAddDrugWithExceptionThenItDoNotAdds();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenTakeDrugThenItDeletes() throws ServiceException, SQLException {
        super.whenTakeDrugThenItDeletes();
        verify(this.hibernateMocker.getSession(), times(1))
                .delete(any(Drug.class));
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenTakeDrugWithExceptionThenItDoNotDeletes() throws ServiceException, SQLException {
        doThrow(HibernateException.class).when(this.hibernateMocker.getSession()).delete(any(Drug.class));
        super.whenTakeDrugWithExceptionThenItDoNotDeletes();
    }
}