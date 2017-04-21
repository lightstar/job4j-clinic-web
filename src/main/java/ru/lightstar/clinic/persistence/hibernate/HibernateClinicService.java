package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.persistence.PersistentClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.pet.Pet;
import ru.lightstar.clinic.pet.Sex;

import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;
import java.util.List;

/**
 * Database-aware <code>ClinicService</code> implementation which uses hibernate.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class HibernateClinicService extends PersistentClinicService {

    /**
     * Hibernate's session factory.
     */
    private final SessionFactory sessionFactory;

    /**
     * Constructs <code>HibernateClinicService</code> object.
     */
    public HibernateClinicService(final ServletContext context) {
        super();
        this.sessionFactory = (SessionFactory) context.getAttribute("sessionFactory");
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized void loadClinic() {
        try (final Session session = this.sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                final List<Client> list = session.createQuery("from Client").list();
                for (final Client client : list) {
                    final Pet pet = client.getPet();
                    client.setPet(Pet.NONE);

                    super.addClient(client.getPosition(), client);
                    if (pet != null) {
                        super.setClientPet(client, pet);
                    }
                }
                transaction.commit();
            } catch (PersistenceException | ServiceException | NameException e) {
                throw new IllegalStateException("Can't load data from database", e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Client addClient(final int position, final String name, final String email, final String phone)
            throws ServiceException, NameException {
        final Client client = super.addClient(position, name, email, phone);

        try (final Session session = this.sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                session.save(client);
                transaction.commit();
            } catch (PersistenceException e) {
                this.undoAddClient(client);
                throw new ServiceException(String.format("Can't insert client into database: %s", e.getMessage()));
            }
        }

        return client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Pet setClientPet(final String name, final String petType, final String petName,
                                         final int petAge, final Sex petSex)
            throws ServiceException, NameException {
        final Client client = this.findClientByName(name);
        final Pet oldPet = client.getPet();
        final Pet pet = super.setClientPet(client, petType, petName, petAge, petSex);

        try (final Session session = this.sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(pet);
                transaction.commit();
            } catch (PersistenceException e) {
                this.undoSetClientPet(client, oldPet);
                throw new ServiceException(String.format("Can't insert client's pet into database: %s", e.getMessage()));
            }
        }

        return pet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void updateClient(final String name, final String newName,
                                          final String newEmail, final String newPhone)
            throws ServiceException, NameException {
        final Client client = this.findClientByName(name);
        final String oldEmail = client.getEmail();
        final String oldPhone = client.getPhone();
        super.updateClient(client, newName, newEmail, newPhone);

        try (final Session session = this.sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                session.update(client);
                transaction.commit();
            } catch (PersistenceException e) {
                this.undoUpdateClient(client, name, oldEmail, oldPhone);
                throw new ServiceException(String.format("Can't update client's name in database: %s", e.getMessage()));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void updateClientPet(final String name, final String petName,
                                             final int petAge, final Sex petSex)
            throws ServiceException, NameException {
        final Client client = this.findClientByName(name);
        final Pet pet = client.getPet();
        final String oldPetName = pet.getName();
        final int oldPetAge = pet.getAge();
        final Sex oldPetSex = pet.getSex();
        super.updateClientPet(client, petName, petAge, petSex);

        try (final Session session = this.sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                session.update(pet);
                transaction.commit();
            } catch (PersistenceException e) {
                this.undoUpdateClientPet(client, oldPetName, oldPetAge, oldPetSex);
                throw new ServiceException(String.format("Can't update client pet's name in database: %s", e.getMessage()));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void deleteClientPet(final String name) throws ServiceException {
        final Client client = this.findClientByName(name);
        final Pet pet = client.getPet();
        super.deleteClientPet(client);

        try (final Session session = this.sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                session.delete(pet);
                transaction.commit();
            } catch (PersistenceException e) {
                this.undoDeleteClientPet(client, pet);
                throw new ServiceException(String.format("Can't delete client's pet from database: %s", e.getMessage()));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void deleteClient(final String name) throws ServiceException {
        final Client client = this.findClientByName(name);
        super.deleteClient(client);

        try (final Session session = this.sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                session.delete(client);
                transaction.commit();
            } catch (PersistenceException e) {
                this.undoDeleteClient(client);
                throw new ServiceException(String.format("Can't delete client from database: %s", e.getMessage()));
            }
        }
    }
}
