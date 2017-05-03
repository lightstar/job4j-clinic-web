package ru.lightstar.clinic.persistence;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.pet.Pet;
import ru.lightstar.clinic.pet.Sex;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Base class for testing <code>PersistentClinicService</code> subclasses.
 *
 * @author LightStar
 * @since 0.0.1
 */
public abstract class PersistentClinicServiceTest extends Mockito {

    /**
     * <code>PersistentClinicService</code> object used in all tests. Must be set in subclasses.
     */
    protected PersistentClinicService clinicService;

    /**
     * Test correctness of <code>loadClinic</code> method.
     */
    public void whenLoadClinicThenItLoads() throws ServiceException {
        final Client vasya = this.clinicService.findClientByName("Vasya");
        final Client masha = this.clinicService.findClientByName("Masha");
        final Client vova = this.clinicService.findClientByName("Vova");

        assertThat(vasya.getId(), is(1));
        assertThat(vasya.getName(), is("Vasya"));
        assertThat(vasya.getEmail(), is("vasya@mail.ru"));
        assertThat(vasya.getPhone(), is("22222"));
        assertThat(vasya.getPosition(), is(2));

        assertThat(vasya.getPet().getId(), is(1));
        assertThat(vasya.getPet().getType(), is("dog"));
        assertThat(vasya.getPet().getName(), is("Bobik"));
        assertThat(vasya.getPet().getAge(), is(10));
        assertThat(vasya.getPet().getSex(), is(Sex.M));

        assertThat(masha.getId(), is(2));
        assertThat(masha.getName(), is("Masha"));
        assertThat(masha.getEmail(), is("masha@mail.ru"));
        assertThat(masha.getPhone(), is("123456"));
        assertThat(masha.getPosition(), is(5));

        assertThat(masha.getPet().getId(), is(2));
        assertThat(masha.getPet().getType(), is("cat"));
        assertThat(masha.getPet().getName(), is("Murka"));
        assertThat(masha.getPet().getAge(), is(6));
        assertThat(masha.getPet().getSex(), is(Sex.F));

        assertThat(vova.getId(), is(3));
        assertThat(vova.getName(), is("Vova"));
        assertThat(vova.getEmail(), is("vova@mail.ru"));
        assertThat(vova.getPhone(), is("55555"));
        assertThat(vova.getPosition(), is(6));

        assertThat(vova.getPet(), is(Pet.NONE));
    }

    /**
     * Test correctness of <code>addClient</code> method.
     */
    public void whenAddClientThenItAdds() throws ServiceException, NameException, SQLException {
        final Role role = new Role("client");
        role.setId(2);
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123", role);
        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getId(), is(4));
        assertThat(petya.getName(), is("Petya"));
        assertThat(petya.getEmail(), is("petya@mail.ru"));
        assertThat(petya.getPhone(), is("123123"));
        assertThat(petya.getPosition(), is(7));
        assertThat(petya.getRole().getName(), is("client"));
        assertThat(petya.getRole().getId(), is(2));
    }

    /**
     * Test correctness of <code>addClient</code> method when exception is thrown.
     */
    public void whenAddClientWithExceptionThenItDoNotAdds() throws ServiceException, NameException, SQLException {
        Throwable expectedException = null;
        try {
            this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123", new Role());
        } catch (ServiceException e) {
            expectedException = e;
        }

        assertThat(expectedException, instanceOf(ServiceException.class));
        this.clinicService.findClientByName("Petya");
    }

    /**
     * Test correctness of <code>setClientPet</code> method.
     */
    public void whenSetClientPetThenItSets() throws ServiceException, NameException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123", new Role());
        this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);

        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getPet().getId(), is(5));
        assertThat(petya.getPet().getType(), is("fish"));
        assertThat(petya.getPet().getName(), is("Beauty"));
        assertThat(petya.getPet().getAge(), is(2));
        assertThat(petya.getPet().getSex(), is(Sex.F));
    }

    /**
     * Test correctness of <code>setClientPet</code> method when exception is thrown.
     */
    public void whenSetClientPetWithExceptionThenItDoNotSets()
            throws ServiceException, NameException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123", new Role());

        Throwable expectedException = null;
        try {
            this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);
        } catch (ServiceException e) {
            expectedException = e;
        }

        assertThat(expectedException, instanceOf(ServiceException.class));

        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getPet(), is(Pet.NONE));
    }

    /**
     * Test correctness of <code>setClientPet</code> method when exception is thrown
     * and client previously had pet.
     */
    public void whenSetClientPetWithExceptionAndClientHavingPetThenItDoNotSets()
            throws ServiceException, NameException, SQLException {
        Throwable expectedException = null;
        try {
            this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);
        } catch (ServiceException e) {
            expectedException = e;
        }

        assertThat(expectedException, instanceOf(ServiceException.class));

        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getPet().getId(), is(5));
        assertThat(petya.getPet().getType(), is("fish"));
        assertThat(petya.getPet().getName(), is("Beauty"));
        assertThat(petya.getPet().getAge(), is(2));
        assertThat(petya.getPet().getSex(), is(Sex.F));
    }

    /**
     * Test correctness of <code>updateClient</code> method.
     */
    public void whenUpdateClientThenItUpdates() throws NameException, ServiceException, SQLException {
        final Role role = new Role("client");
        role.setId(2);
        final Role newRole = new Role("admin");
        newRole.setId(1);
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123", role);
        this.clinicService.updateClient("Petya", "Sasha", "sasha@mail.ru", "456456",
                newRole);

        final Client vova = this.clinicService.findClientByName("Sasha");
        assertThat(vova.getId(), is(4));
        assertThat(vova.getName(), is("Sasha"));
        assertThat(vova.getEmail(), is("sasha@mail.ru"));
        assertThat(vova.getPhone(), is("456456"));
        assertThat(vova.getPosition(), is(7));
        assertThat(vova.getRole().getName(), is("admin"));
        assertThat(vova.getRole().getId(), is(1));
    }

    /**
     * Test correctness of <code>updateClient</code> method when exception is thrown.
     */
    public void whenUpdateClientWithExceptionThenItDoNotUpdates()
            throws NameException, ServiceException, SQLException {
        final Role role = new Role("client");
        role.setId(2);
        final Role newRole = new Role("admin");
        newRole.setId(1);
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123", role);

        Throwable expectedException = null;
        try {
            this.clinicService.updateClient("Petya", "Sasha", "sasha@mail.ru",
                    "456456", newRole);
        } catch (ServiceException e) {
            expectedException = e;
        }

        assertThat(expectedException, instanceOf(ServiceException.class));

        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getId(), is(4));
        assertThat(petya.getName(), is("Petya"));
        assertThat(petya.getEmail(), is("petya@mail.ru"));
        assertThat(petya.getPhone(), is("123123"));
        assertThat(petya.getPosition(), is(7));
        assertThat(petya.getRole().getName(), is("client"));
        assertThat(petya.getRole().getId(), is(2));
    }

    /**
     * Test correctness of <code>updateClientPet</code> method.
     */
    public void whenUpdateClientPetThenItUpdates() throws NameException, ServiceException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123", new Role());
        this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);
        this.clinicService.updateClientPet("Petya", "Summer", 3, Sex.M);

        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getPet().getId(), is(5));
        assertThat(petya.getPet().getType(), is("fish"));
        assertThat(petya.getPet().getName(), is("Summer"));
        assertThat(petya.getPet().getAge(), is(3));
        assertThat(petya.getPet().getSex(), is(Sex.M));
    }

    /**
     * Test correctness of <code>updateClientPet</code> method when exception is thrown.
     */
    public void whenUpdateClientPetWithExceptionThenItDoNotUpdates()
            throws NameException, ServiceException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123", new Role());
        this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);

        Throwable expectedException = null;
        try {
            this.clinicService.updateClientPet("Petya", "Summer", 3, Sex.M);
        } catch (ServiceException e) {
            expectedException = e;
        }

        assertThat(expectedException, instanceOf(ServiceException.class));

        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getPet().getId(), is(5));
        assertThat(petya.getPet().getType(), is("fish"));
        assertThat(petya.getPet().getName(), is("Beauty"));
        assertThat(petya.getPet().getAge(), is(2));
        assertThat(petya.getPet().getSex(), is(Sex.F));
    }

    /**
     * Test correctness of <code>deleteClientPet</code> method.
     */
    @Test
    public void whenDeleteClientPetThenItDeletes() throws NameException, ServiceException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123", new Role());
        this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);
        this.clinicService.deleteClientPet("Petya");

        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getPet(), is(Pet.NONE));
    }

    /**
     * Test correctness of <code>deleteClientPet</code> method when exception is thrown.
     */
    @Test
    public void whenDeleteClientPetWithExceptionThenItDoNotDeletes()
            throws NameException, ServiceException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123", new Role());
        this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);

        Throwable expectedException = null;
        try {
            this.clinicService.deleteClientPet("Petya");
        } catch (ServiceException e) {
            expectedException = e;
        }

        assertThat(expectedException, instanceOf(ServiceException.class));

        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getPet().getId(), is(5));
        assertThat(petya.getPet().getType(), is("fish"));
        assertThat(petya.getPet().getName(), is("Beauty"));
        assertThat(petya.getPet().getAge(), is(2));
        assertThat(petya.getPet().getSex(), is(Sex.F));
    }

    /**
     * Test correctness of <code>deleteClient</code> method.
     */
    public void whenDeleteClientThenItDeletes() throws NameException, ServiceException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123", new Role());
        this.clinicService.deleteClient("Petya");

        Throwable expectedException = null;
        try {
            this.clinicService.findClientByName("Petya");
        } catch (ServiceException e) {
            expectedException = e;
        }

        assertThat(expectedException, instanceOf(ServiceException.class));
    }

    /**
     * Test correctness of <code>deleteClient</code> method when exception is thrown.
     */
    public void whenDeleteClientWithExceptionThenItDoNotDeletes()
            throws NameException, ServiceException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123", new Role());

        Throwable expectedException = null;
        try {
            this.clinicService.deleteClient("Petya");
        } catch (ServiceException e) {
            expectedException = e;
        }

        assertThat(expectedException, instanceOf(ServiceException.class));

        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getId(), is(4));
        assertThat(petya.getName(), is("Petya"));
        assertThat(petya.getEmail(), is("petya@mail.ru"));
        assertThat(petya.getPhone(), is("123123"));
        assertThat(petya.getPosition(), is(7));
    }

    /**
     * Helper method used to add test client with test pet.
     *
     * @throws NameException shouldn't be thrown.
     * @throws ServiceException shouldn't be thrown.
     */
    protected void addClientWithPet() throws NameException, ServiceException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123", new Role());
        this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);
    }
}