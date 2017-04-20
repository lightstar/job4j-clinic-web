package ru.lightstar.clinic;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.mockito.Mockito;
import ru.lightstar.clinic.drug.Aspirin;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.drug.Glucose;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.pet.Cat;
import ru.lightstar.clinic.pet.Dog;
import ru.lightstar.clinic.pet.Pet;
import ru.lightstar.clinic.pet.Sex;

import java.util.Arrays;

/**
 * Helper object used to get mocked hibernate session factory with some test data.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class SessionFactoryMocker extends Mockito {

    /**
     * Mocked session object.
     */
    private Session session;

    /**
     * Mocked transaction object.
     */
    private Transaction transaction;

    /**
     * Get mocked hibernate session factory with test data.
     *
     * @return mocked hibernate session factory.
     */
    public SessionFactory getSessionFactory() {
        final SessionFactory sessionFactory = mock(SessionFactory.class);
        this.session = mock(Session.class);
        this.transaction = mock(Transaction.class);

        when(sessionFactory.openSession()).thenReturn(this.session);
        when(this.session.beginTransaction()).thenReturn(this.transaction);

        this.mockLoadClinicQuery(this.session);
        this.mockAddClientQuery(this.session);
        this.mockSetClientPetQuery(this.session);
        this.mockLoadDrugsQuery(this.session);

        return sessionFactory;
    }

    /**
     * Get mocked session object.
     *
     * @return mocked session object.
     */
    public Session getSession() {
        return this.session;
    }

    /**
     * Get mocked transaction object.
     *
     * @return mocked transaction object.
     */
    public Transaction getTransaction() {
        return this.transaction;
    }

    /**
     * Mocking load clinic query.
     *
     * @param session mocked session.
     */
    private void mockLoadClinicQuery(final Session session) {
        final Query loadClinicQuery = mock(Query.class);
        when(session.createQuery("from Client")).thenReturn(loadClinicQuery);

        final Client vasya = new Client("Vasya", new Dog("Bobik", new DummyOutput()), 2);
        vasya.setId(1);
        vasya.setEmail("vasya@mail.ru");
        vasya.setPhone("22222");

        vasya.getPet().setClient(vasya);
        vasya.getPet().setId(1);
        vasya.getPet().setAge(10);
        vasya.getPet().setSex(Sex.M);

        final Client masha = new Client("Masha", new Cat("Murka", new DummyOutput()), 5);
        masha.setId(2);
        masha.setEmail("masha@mail.ru");
        masha.setPhone("123456");

        masha.getPet().setClient(masha);
        masha.getPet().setId(2);
        masha.getPet().setAge(6);
        masha.getPet().setSex(Sex.F);

        final Client vova = new Client("Vova", null, 6);
        vova.setId(3);
        vova.setEmail("vova@mail.ru");
        vova.setPhone("55555");

        when(loadClinicQuery.list()).thenReturn(Arrays.asList(vasya, masha, vova));
    }

    /**
     * Mocking add client query.
     *
     * @param session mocked session.
     */
    private void mockAddClientQuery(final Session session) {
        doAnswer(invocation -> {
            final Object object = invocation.getArguments()[0];
            if (object instanceof Client) {
                ((Client) object).setId(4);
            } else if (object instanceof Drug) {
                ((Drug) object).setId(6);
            }
            return null;
        }).when(session).save(any(Client.class));
    }

    /**
     * Mocking set client pet query.
     *
     * @param session mocked session.
     */
    private void mockSetClientPetQuery(final Session session) {
        doAnswer(invocation -> {
            final Pet pet = invocation.getArgumentAt(0, Pet.class);
            pet.setId(5);
            return null;
        }).when(session).saveOrUpdate(any(Pet.class));
    }

    /**
     * Mocking load drugs query.
     *
     * @param session mocked session.
     */
    private void mockLoadDrugsQuery(final Session session) {
        final Query loadDrugsQuery = mock(Query.class);
        when(session.createQuery("from Drug")).thenReturn(loadDrugsQuery);
        when(loadDrugsQuery.list()).thenReturn(Arrays.asList(new Aspirin(), new Aspirin(),
                new Glucose(), new Aspirin()));
    }
}
