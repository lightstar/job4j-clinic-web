package ru.lightstar.clinic;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.mockito.Mockito;
import ru.lightstar.clinic.drug.Drug;

import java.util.ArrayList;

/**
 * Helper object used to get mocked hibernate session factory with some test data.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class SessionFactoryMocker extends Mockito {

    /**
     * Get mocked hibernate session factory with test data.
     *
     * @return mocked hibernate session factory.
     */
    public SessionFactory getSessionFactory() {
        final SessionFactory sessionFactory = mock(SessionFactory.class);
        final Session session = mock(Session.class);
        final Transaction transaction = mock(Transaction.class);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);

        final Query loadClinicQuery = mock(Query.class);
        when(session.createQuery("from Client")).thenReturn(loadClinicQuery);
        when(loadClinicQuery.list()).thenReturn(new ArrayList<Client>());

        final Query loadDrugsQuery = mock(Query.class);
        when(session.createQuery("from Drug")).thenReturn(loadDrugsQuery);
        when(loadDrugsQuery.list()).thenReturn(new ArrayList<Drug>());

        return sessionFactory;
    }
}
