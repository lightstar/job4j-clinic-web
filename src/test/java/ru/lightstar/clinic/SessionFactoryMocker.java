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
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Message;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.persistence.hibernate.HibernateMessageService;
import ru.lightstar.clinic.persistence.hibernate.HibernateRoleService;
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
     * Mocked <code>Query</code> object for get all roles query.
     */
    private Query allRolesQuery;

    /**
     * Mocked <code>Query</code> object for get role by name query when name equals to 'client'.
     */
    private Query roleByNameClientQuery;

    /**
     * Mocked <code>Query</code> object for get role by name query when name equals to 'manager'.
     */
    private Query roleByNameManagerQuery;

    /**
     * Mocked <code>Query</code> object for delete role query.
     */
    private Query deleteRoleQuery;

    /**
     * Mocked <code>Query</code> object for checking if role is busy query.
     */
    private Query isRoleBusyQuery;

    /**
     * Mocked <code>Query</code> object for get client messages query.
     */
    private Query clientMessagesQuery;

    /**
     * Mocked <code>Query</code> object for delete client message query.
     */
    private Query deleteMessageQuery;

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
        when(this.session.getTransaction()).thenReturn(this.transaction);

        this.mockLoadClinicQuery(this.session);
        this.mockAddClientQuery(this.session);
        this.mockSetClientPetQuery(this.session);

        this.mockLoadDrugsQuery(this.session);

        this.mockAllRolesQuery(this.session);
        this.mockRoleByNameQuery(this.session);
        this.mockDeleteRoleQuery(this.session);
        this.mockIsRoleBusyQuery(this.session);

        this.mockClientMessagesQuery(this.session);
        this.mockDeleteMessageQuery(this.session);

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
     * Get mocked <code>Query</code> object for get all roles query.
     *
     * @return mocked query object.
     */
    public Query getAllRolesQuery() {
        return this.allRolesQuery;
    }

    /**
     * Get mocked <code>Query</code> object for get role by name query when name equals to 'client'.
     *
     * @return mocked query object.
     */
    public Query getRoleByNameClientQuery() {
        return this.roleByNameClientQuery;
    }

    /**
     * Get mocked <code>Query</code> object for get role by name query when name equals to 'manager'.
     *
     * @return mocked query object.
     */
    public Query getRoleByNameManagerQuery() {
        return this.roleByNameManagerQuery;
    }

    /**
     * Get mocked <code>Query</code> object for delete role query.
     *
     * @return mocked query object.
     */
    public Query getDeleteRoleQuery() {
        return this.deleteRoleQuery;
    }

    /**
     * Get mocked <code>Query</code> object for checking if role is busy query.
     *
     * @return mocked query object.
     */
    public Query getIsRoleBusyQuery() {
        return this.isRoleBusyQuery;
    }

    /**
     * Get mocked <code>Query</code> object for get client messages query.
     *
     * @return mocked query object.
     */
    public Query getClientMessagesQuery() {
        return this.clientMessagesQuery;
    }

    /**
     * Get mocked <code>Query</code> object for delete client message query.
     *
     * @return mocked query object.
     */
    public Query getDeleteMessageQuery() {
        return this.deleteMessageQuery;
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

    /**
     * Mocking get all roles query
     *
     * @param session mocked session.
     */
    private void mockAllRolesQuery(final Session session) {
        this.allRolesQuery = mock(Query.class);
        when(session.createQuery(HibernateRoleService.ALL_ROLES_HQL)).thenReturn(this.allRolesQuery);

        final Role adminRole = new Role("admin");
        adminRole.setId(1);
        final Role clientRole = new Role("client");
        clientRole.setId(2);

        when(this.allRolesQuery.list()).thenReturn(Arrays.asList(adminRole, clientRole));
    }

    /**
     * Mocking get role by name query.
     *
     * @param session mocked session.
     */
    private void mockRoleByNameQuery(final Session session) {
        final Query roleByNameQuery = mock(Query.class);
        when(session.createQuery(HibernateRoleService.ROLE_BY_NAME_HQL))
                .thenReturn(roleByNameQuery);

        this.roleByNameClientQuery = mock(Query.class);
        when(roleByNameQuery.setParameter("name", "client"))
                .thenReturn(this.roleByNameClientQuery);

        final Role clientRole = new Role("client");
        clientRole.setId(2);
        when(this.roleByNameClientQuery.uniqueResult()).thenReturn(clientRole);

        this.roleByNameManagerQuery = mock(Query.class);
        when(roleByNameQuery.setParameter("name", "manager"))
                .thenReturn(this.roleByNameManagerQuery);

        final Role managerRole = new Role("manager");
        managerRole.setId(3);

        when(this.roleByNameManagerQuery.uniqueResult()).thenReturn(managerRole);
    }

    /**
     * Mocking delete role query.
     *
     * @param session mocked session.
     */
    private void mockDeleteRoleQuery(final Session session) {
        this.deleteRoleQuery = mock(Query.class);
        when(session.createQuery(HibernateRoleService.DELETE_ROLE_HQL))
                .thenReturn(this.deleteRoleQuery);
        when(this.deleteRoleQuery.setParameter("name", "client"))
                .thenReturn(this.deleteRoleQuery);
    }

    /**
     * Mocking checking if role is busy query.
     *
     * @param session mocked session.
     */
    private void mockIsRoleBusyQuery(final Session session) {
        this.isRoleBusyQuery = mock(Query.class);
        when(session.createQuery(HibernateRoleService.CLIENT_BY_ROLE_HQL))
                .thenReturn(this.isRoleBusyQuery);
        when(this.isRoleBusyQuery.setParameter("name", "client"))
                .thenReturn(this.isRoleBusyQuery);
        when(this.isRoleBusyQuery.setMaxResults(1))
                .thenReturn(this.isRoleBusyQuery);

        final Client client = new Client("Vasya", Pet.NONE, 0);
        client.setId(1);

        when(this.isRoleBusyQuery.uniqueResult()).thenReturn(client);
    }

    /**
     * Mocking get client messages query
     *
     * @param session mocked session.
     */
    private void mockClientMessagesQuery(final Session session) {
        this.clientMessagesQuery = mock(Query.class);
        when(session.createQuery(HibernateMessageService.CLIENT_MESSAGES_HQL))
                .thenReturn(this.clientMessagesQuery);

        final Message message1 = new Message(Client.NONE, "Test message");
        message1.setId(1);
        final Message message2 = new Message(Client.NONE, "Another test message");
        message2.setId(2);

        doAnswer(invocation -> {
            final Client client = (Client) (invocation.getArguments())[1];
            message1.setClient(client);
            message2.setClient(client);
            return SessionFactoryMocker.this.clientMessagesQuery;
        }).when(this.clientMessagesQuery).setParameter(eq("client"), any(Client.class));

        when(this.clientMessagesQuery.list()).thenReturn(Arrays.asList(message1, message2));
    }

    /**
     * Mocking delete client message query.
     *
     * @param session mocked session.
     */
    private void mockDeleteMessageQuery(final Session session) {
        this.deleteMessageQuery = mock(Query.class);
        when(session.createQuery(HibernateMessageService.DELETE_MESSAGE_HQL))
                .thenReturn(this.deleteMessageQuery);
        when(this.deleteMessageQuery.setParameter(eq("client"), any(Client.class)))
                .thenReturn(this.deleteMessageQuery);
        when(this.deleteMessageQuery.setParameter(eq("id"), anyInt()))
                .thenReturn(this.deleteMessageQuery);
    }
}
