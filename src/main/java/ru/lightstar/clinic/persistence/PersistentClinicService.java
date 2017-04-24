package ru.lightstar.clinic.persistence;

import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.Clinic;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.io.IteratorInput;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.pet.Pet;
import ru.lightstar.clinic.pet.Sex;

/**
 * Database-aware <code>ClinicService</code> implementation.
 *
 * @author LightStar
 * @since 0.0.1
 */
public abstract class PersistentClinicService extends ClinicService {

    /**
     * Clinic size.
     */
    private static final int CLINIC_SIZE = 10;

    /**
     * Constructs <code>PersistentClinicService</code> object.
     */
    public PersistentClinicService() {
        super(new IteratorInput(), new DummyOutput(), new Clinic(CLINIC_SIZE));
    }

    /**
     * Load all data from database to inner clinic object.
     */
    public abstract void loadClinic();

    /**
     * Undo <code>AddClient</code> operation.
     *
     * @param client added client.
     * @throws ServiceException shouldn't be thrown.
     */
    protected void undoAddClient(final Client client) throws ServiceException {
        super.deleteClient(client);
    }

    /**
     * Undo <code>SetClientPet</code> operation.
     *
     * @param client affected client.
     * @param oldPet previous client's pet.
     * @throws ServiceException shouldn't be thrown.
     */
    protected void undoSetClientPet(final Client client, final Pet oldPet) throws ServiceException {
        if (oldPet == Pet.NONE) {
            super.deleteClientPet(client);
        } else {
            super.setClientPet(client, oldPet);
        }
    }

    /**
     * Undo <code>UpdateClient</code> operation.
     *
     * @param client affected client.
     * @param oldName previous client's name.
     * @param oldEmail previous client's email.
     * @param oldPhone previous client's phone.
     * @param oldRole previous client's role.
     * @throws ServiceException shouldn't be thrown.
     * @throws NameException shouldn't be thrown.
     */
    protected void undoUpdateClient(final Client client, final String oldName, final String oldEmail,
                                    final String oldPhone, final Role oldRole)
            throws ServiceException, NameException {
        super.updateClient(client, oldName, oldEmail, oldPhone, oldRole);
    }

    /**
     * Undo <code>UpdateClientPet</code> operation.
     *
     * @param client affected client.
     * @param oldPetName previous client pet's name.
     * @param oldPetAge previous client pet's age.
     * @param oldPetSex previous client pet's sex.
     * @throws NameException shouldn't be thrown.
     * @throws ServiceException shouldn't be thrown.
     */
    protected void undoUpdateClientPet(final Client client, final String oldPetName, final int oldPetAge,
                                       final Sex oldPetSex)
            throws NameException, ServiceException {
        super.updateClientPet(client, oldPetName, oldPetAge, oldPetSex);
    }

    /**
     * Undo <code>DeleteClientPet</code> operation.
     *
     * @param client affected client.
     * @param oldPet previous client's pet.
     * @throws ServiceException shouldn't be thrown.
     */
    protected void undoDeleteClientPet(final Client client, final Pet oldPet) throws ServiceException {
        super.setClientPet(client, oldPet);
    }

    /**
     * Undo <code>DeleteClient</code> operation.
     *
     * @param client deleted client.
     * @throws ServiceException shouldn't be thrown.
     */
    protected void undoDeleteClient(final Client client) throws ServiceException {
        final Pet pet = client.getPet();
        try {
            super.addClient(client.getPosition(), client);
            if (pet != Pet.NONE) {
                client.setPet(Pet.NONE);
                super.setClientPet(client, pet);
            }
        } catch (NameException e) {
            throw new IllegalStateException(e);
        }
    }
}
