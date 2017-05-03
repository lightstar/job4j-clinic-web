package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.hibernate5.HibernateTemplate;
import ru.lightstar.clinic.SessionFactoryMocker;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.persistence.PersistentClinicServiceTest;
import ru.lightstar.clinic.pet.Pet;

import java.sql.SQLException;

/**
 * <code>HibernateClinicService</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class HibernateClinicServiceTest extends PersistentClinicServiceTest {

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
        this.clinicService = new HibernateClinicService(hibernateTemplate);
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenLoadClinicThenItLoads() throws ServiceException {
        super.whenLoadClinicThenItLoads();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenAddClientThenItAdds() throws ServiceException, NameException, SQLException {
        super.whenAddClientThenItAdds();
        verify(this.hibernateMocker.getSession(), times(1))
                .save(any(Client.class));
    }


    /**
     * {@inheritDoc}
     */
    @Test(expected = ServiceException.class)
    @Override
    public void whenAddClientWithExceptionThenItDoNotAdds() throws ServiceException, NameException, SQLException {
        doThrow(HibernateException.class).when(this.hibernateMocker.getSession()).save(any(Client.class));
        super.whenAddClientWithExceptionThenItDoNotAdds();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenSetClientPetThenItSets() throws ServiceException, NameException, SQLException {
        super.whenSetClientPetThenItSets();
        verify(this.hibernateMocker.getSession(), times(1))
                .saveOrUpdate(any(Pet.class));
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenSetClientPetWithExceptionThenItDoNotSets()
            throws ServiceException, NameException, SQLException {
        doThrow(HibernateException.class).when(this.hibernateMocker.getSession()).saveOrUpdate(any(Pet.class));
        super.whenSetClientPetWithExceptionThenItDoNotSets();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenSetClientPetWithExceptionAndClientHavingPetThenItDoNotSets()
            throws ServiceException, NameException, SQLException {
        this.addClientWithPet();
        doThrow(HibernateException.class).when(this.hibernateMocker.getSession()).saveOrUpdate(any(Pet.class));
        super.whenSetClientPetWithExceptionAndClientHavingPetThenItDoNotSets();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenUpdateClientThenItUpdates() throws NameException, ServiceException, SQLException {
        super.whenUpdateClientThenItUpdates();
        verify(this.hibernateMocker.getSession(), times(1))
                .update(any(Client.class));
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenUpdateClientWithExceptionThenItDoNotUpdates()
            throws NameException, ServiceException, SQLException {
        doThrow(HibernateException.class).when(this.hibernateMocker.getSession()).update(any(Client.class));
        super.whenUpdateClientWithExceptionThenItDoNotUpdates();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenUpdateClientPetThenItUpdates() throws NameException, ServiceException, SQLException {
        super.whenUpdateClientPetThenItUpdates();
        verify(this.hibernateMocker.getSession(), times(1))
                .update(any(Pet.class));
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenUpdateClientPetWithExceptionThenItDoNotUpdates()
            throws NameException, ServiceException, SQLException {
        doThrow(HibernateException.class).when(this.hibernateMocker.getSession()).update(any(Pet.class));
        super.whenUpdateClientPetWithExceptionThenItDoNotUpdates();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenDeleteClientPetThenItDeletes() throws NameException, ServiceException, SQLException {
        super.whenDeleteClientPetThenItDeletes();
        verify(this.hibernateMocker.getSession(), times(1))
                .delete(any(Pet.class));
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenDeleteClientPetWithExceptionThenItDoNotDeletes()
            throws NameException, ServiceException, SQLException {
        doThrow(HibernateException.class).when(this.hibernateMocker.getSession()).delete(any(Pet.class));
        super.whenDeleteClientPetWithExceptionThenItDoNotDeletes();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenDeleteClientThenItDeletes() throws NameException, ServiceException, SQLException {
        super.whenDeleteClientThenItDeletes();
        verify(this.hibernateMocker.getSession(), times(1))
                .delete(any(Client.class));
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void whenDeleteClientWithExceptionThenItDoNotDeletes()
            throws NameException, ServiceException, SQLException {
        doThrow(HibernateException.class).when(this.hibernateMocker.getSession()).delete(any(Client.class));
        super.whenDeleteClientWithExceptionThenItDoNotDeletes();
    }
}