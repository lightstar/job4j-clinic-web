package ru.lightstar.clinic.persistence.jdbc;

import org.junit.Test;
import org.mockito.Mockito;
import ru.lightstar.clinic.Client;
import ru.lightstar.clinic.JdbcConnectionMocker;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.pet.Pet;
import ru.lightstar.clinic.pet.Sex;

import javax.servlet.ServletContext;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * <code>JdbcClinicService</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcClinicServiceTest extends Mockito {

    /**
     * Helper object used to mock jdbc connection.
     */
    private final JdbcConnectionMocker jdbcMocker;

    /**
     * <code>JdbcClinicService</code> object used in all tests.
     */
    private final JdbcClinicService clinicService;

    /**
     * Constructs <code>JdbcClinicServiceTest</code> object.
     */
    public JdbcClinicServiceTest() {
        final ServletContext context = mock(ServletContext.class);
        this.jdbcMocker = new JdbcConnectionMocker();
        final Connection connection = jdbcMocker.getConnection();
        when(context.getAttribute("jdbcConnection")).thenReturn(connection);
        this.clinicService = new JdbcClinicService(context);
    }

    /**
     * Test correctness of <code>loadClinic</code> method.
     */
    @Test
    public void whenLoadClinicThenItLoads() throws ServiceException {
        this.clinicService.loadClinic();

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
    @Test
    public void whenAddClientThenItAdds() throws ServiceException, NameException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123");

        verify(this.jdbcMocker.getAddClientStatement(), times(1))
                .setInt(1, 7);
        verify(this.jdbcMocker.getAddClientStatement(), times(1))
                .setString(2, "Petya");
        verify(this.jdbcMocker.getAddClientStatement(), times(1))
                .setString(3, "petya@mail.ru");
        verify(this.jdbcMocker.getAddClientStatement(), times(1))
                .setString(4, "123123");
        verify(this.jdbcMocker.getAddClientStatement(), times(1))
                .executeUpdate();
        verify(this.jdbcMocker.getGeneratedKeyForAddClientResultSet(), times(1))
                .getInt(1);

        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getId(), is(4));
        assertThat(petya.getName(), is("Petya"));
        assertThat(petya.getEmail(), is("petya@mail.ru"));
        assertThat(petya.getPhone(), is("123123"));
        assertThat(petya.getPosition(), is(7));
    }

    /**
     * Test correctness of <code>addClient</code> method when <code>SQLException</code> is thrown.
     */
    @Test(expected = ServiceException.class)
    public void whenAddClientWithSQLExceptionThenItDoNotAdds() throws ServiceException, NameException, SQLException {
        doThrow(SQLException.class).when(this.jdbcMocker.getAddClientStatement()).executeUpdate();

        Throwable expectedException = null;
        try {
            this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123");
        } catch (ServiceException e) {
            expectedException = e;
        }

        assertThat(expectedException, instanceOf(ServiceException.class));
        this.clinicService.findClientByName("Petya");
    }

    /**
     * Test correctness of <code>setClientPet</code> method.
     */
    @Test
    public void whenSetClientPetThenItSets() throws ServiceException, NameException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123");
        this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);

        verify(this.jdbcMocker.getSetClientPetStatement(), times(1))
                .setInt(1, 4);
        verify(this.jdbcMocker.getSetClientPetStatement(), times(1))
                .setString(2, "fish");
        verify(this.jdbcMocker.getSetClientPetStatement(), times(1))
                .setString(3, "Beauty");
        verify(this.jdbcMocker.getSetClientPetStatement(), times(1))
                .setInt(4, 2);
        verify(this.jdbcMocker.getSetClientPetStatement(), times(1))
                .setString(5, "f");
        verify(this.jdbcMocker.getSetClientPetStatement(), times(1))
                .executeUpdate();
        verify(this.jdbcMocker.getGeneratedKeyForSetClientPetResultSet(), times(1))
                .getInt(1);

        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getPet().getId(), is(5));
        assertThat(petya.getPet().getType(), is("fish"));
        assertThat(petya.getPet().getName(), is("Beauty"));
        assertThat(petya.getPet().getAge(), is(2));
        assertThat(petya.getPet().getSex(), is(Sex.F));
    }

    /**
     * Test correctness of <code>setClientPet</code> method when <code>SQLException</code> is thrown.
     */
    @Test
    public void whenSetClientPetWithSQLExceptionThenItDoNotSets()
            throws ServiceException, NameException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123");

        doThrow(SQLException.class).when(this.jdbcMocker.getSetClientPetStatement()).executeUpdate();

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
     * Test correctness of <code>setClientPet</code> method when <code>SQLException</code> is thrown
     * and client previously had pet.
     */
    @Test
    public void whenSetClientPetWithSQLExceptionAndClientHavingPetThenItDoNotSets()
            throws ServiceException, NameException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123");
        this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);

        doThrow(SQLException.class).when(this.jdbcMocker.getSetClientPetStatement()).executeUpdate();

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
    @Test
    public void whenUpdateClientThenItUpdates() throws NameException, ServiceException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123");
        this.clinicService.updateClient("Petya", "Vova", "vova@mail.ru", "456456");

        verify(this.jdbcMocker.getUpdateClientStatement(), times(1))
                .setString(1, "Vova");
        verify(this.jdbcMocker.getUpdateClientStatement(), times(1))
                .setString(2, "vova@mail.ru");
        verify(this.jdbcMocker.getUpdateClientStatement(), times(1))
                .setString(3, "456456");
        verify(this.jdbcMocker.getUpdateClientStatement(), times(1))
                .setString(4, "Petya");
        verify(this.jdbcMocker.getUpdateClientStatement(), times(1))
                .executeUpdate();

        final Client vova = this.clinicService.findClientByName("Vova");
        assertThat(vova.getId(), is(4));
        assertThat(vova.getName(), is("Vova"));
        assertThat(vova.getEmail(), is("vova@mail.ru"));
        assertThat(vova.getPhone(), is("456456"));
        assertThat(vova.getPosition(), is(7));
    }

    /**
     * Test correctness of <code>updateClient</code> method when <code>SQLException</code> is thrown.
     */
    @Test
    public void whenUpdateClientWithSQLExceptionThenItDoNotUpdates()
            throws NameException, ServiceException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123");

        doThrow(SQLException.class).when(this.jdbcMocker.getUpdateClientStatement()).executeUpdate();

        Throwable expectedException = null;
        try {
            this.clinicService.updateClient("Petya", "Vova", "vova@mail.ru", "456456");
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
     * Test correctness of <code>updateClientPet</code> method.
     */
    @Test
    public void whenUpdateClientPetThenItUpdates() throws NameException, ServiceException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123");
        this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);
        this.clinicService.updateClientPet("Petya", "Summer", 3, Sex.M);

        verify(this.jdbcMocker.getUpdateClientPetStatement(), times(1))
                .setString(1, "Summer");
        verify(this.jdbcMocker.getUpdateClientPetStatement(), times(1))
                .setInt(2, 3);
        verify(this.jdbcMocker.getUpdateClientPetStatement(), times(1))
                .setString(3, "m");
        verify(this.jdbcMocker.getUpdateClientPetStatement(), times(1))
                .setInt(4, 4);
        verify(this.jdbcMocker.getUpdateClientPetStatement(), times(1))
                .executeUpdate();

        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getPet().getId(), is(5));
        assertThat(petya.getPet().getType(), is("fish"));
        assertThat(petya.getPet().getName(), is("Summer"));
        assertThat(petya.getPet().getAge(), is(3));
        assertThat(petya.getPet().getSex(), is(Sex.M));
    }

    /**
     * Test correctness of <code>updateClientPet</code> method when <code>SQLException</code> is thrown.
     */
    @Test
    public void whenUpdateClientPetWithSQLExceptionThenItDoNotUpdates()
            throws NameException, ServiceException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123");
        this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);

        doThrow(SQLException.class).when(this.jdbcMocker.getUpdateClientPetStatement()).executeUpdate();

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
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123");
        this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);
        this.clinicService.deleteClientPet("Petya");

        verify(this.jdbcMocker.getDeleteClientPetStatement(), times(1))
                .setInt(1, 4);
        verify(this.jdbcMocker.getDeleteClientPetStatement(), times(1))
                .executeUpdate();

        final Client petya = this.clinicService.findClientByName("Petya");
        assertThat(petya.getPet(), is(Pet.NONE));
    }

    /**
     * Test correctness of <code>deleteClientPet</code> method when <code>SQLException</code> is thrown.
     */
    @Test
    public void whenDeleteClientPetWithSQLExceptionThenItDoNotDeletes()
            throws NameException, ServiceException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123");
        this.clinicService.setClientPet("Petya", "fish", "Beauty", 2, Sex.F);

        doThrow(SQLException.class).when(this.jdbcMocker.getDeleteClientPetStatement()).executeUpdate();

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
    @Test(expected = ServiceException.class)
    public void whenDeleteClientThenItDeletes() throws NameException, ServiceException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123");
        this.clinicService.deleteClient("Petya");

        verify(this.jdbcMocker.getDeleteClientStatement(), times(1))
                .setInt(1, 4);
        verify(this.jdbcMocker.getDeleteClientStatement(), times(1))
                .executeUpdate();

        this.clinicService.findClientByName("Petya");
    }

    /**
     * Test correctness of <code>deleteClient</code> method when <code>SQLException</code> is thrown.
     */
    @Test
    public void whenDeleteClientWithSQLExceptionThenItDoNotDeletes()
            throws NameException, ServiceException, SQLException {
        this.clinicService.addClient(7, "Petya", "petya@mail.ru", "123123");

        doThrow(SQLException.class).when(this.jdbcMocker.getDeleteClientStatement()).executeUpdate();

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
}