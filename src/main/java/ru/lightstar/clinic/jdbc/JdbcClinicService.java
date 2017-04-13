package ru.lightstar.clinic.jdbc;

import ru.lightstar.clinic.Client;
import ru.lightstar.clinic.Clinic;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.io.IteratorInput;
import ru.lightstar.clinic.pet.Pet;

import javax.servlet.ServletContext;
import java.sql.*;

/**
 * Database-aware <code>ClinicService</code> implementation.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcClinicService extends ClinicService {

    /**
     * SQL used to load all client data from database.
     */
    public static final String LOAD_SQL =
            "SELECT client.id, client.position, client.name, " +
                    "pet.id AS petId, pet.type AS petType, pet.name AS petName " +
                    "FROM client LEFT JOIN pet ON client.id = pet.client_id";

    /**
     * SQL used to add new client to database.
     */
    public static final String ADD_CLIENT_SQL = "INSERT INTO client (`position`, `name`) VALUES (?,?)";

    /**
     * SQL used to set client's pet in database.
     */
    public static final String SET_CLIENT_PET_SQL = "REPLACE INTO pet (`client_id`, `type`, `name`) VALUES (?,?,?)";

    /**
     * SQL used to update client's name in database.
     */
    public static final String UPDATE_CLIENT_NAME_SQL = "UPDATE client SET name = ? WHERE name = ?";

    /**
     * SQL used to update client pet's name in database.
     */
    public static final String UPDATE_CLIENT_PET_NAME_SQL = "UPDATE pet SET name = ? WHERE client_id = ?";

    /**
     * SQL used to delete client's pet from database.
     */
    public static final String DELETE_PET_SQL = "DELETE FROM pet WHERE client_id = ?";

    /**
     * SQL used to delete client from database.
     * All his pets will be deleted automatically due to foreign key constraint.
     */
    public static final String DELETE_CLIENT_SQL = "DELETE FROM client WHERE id = ?";

    /**
     * Clinic size.
     */
    private static final int CLINIC_SIZE = 10;

    /**
     * Jdbc connection used by this service.
     */
    private final Connection connection;

    /**
     * Constructs <code>JdbcClinicService</code> object.
     */
    public JdbcClinicService(final ServletContext context) {
        super(new IteratorInput(), new DummyOutput(), new Clinic(CLINIC_SIZE));
        this.connection = (Connection) context.getAttribute("jdbcConnection");
    }

    /**
     * Load all data from database to inner clinic object.
     */
    public void loadClinic() {
        try (final Statement statement = this.connection.createStatement()) {
            try (final ResultSet rs = statement.executeQuery(LOAD_SQL)) {
                while (rs.next()) {
                    final int id = rs.getInt("id");
                    final int position = rs.getInt("position") - 1;
                    final String name = rs.getString("name");
                    final int petId = rs.getInt("petId");
                    final String petType = rs.getString("petType");
                    final String petName = rs.getString("petName");

                    final Pet pet = petType != null ? this.getPetFactory().create(petType, petName) : Pet.NONE;
                    pet.setId(petId);
                    final Client client = new Client(name, pet, position);
                    client.setId(id);

                    this.getClinic().addClient(position, client);
                    if (pet != Pet.NONE) {
                        this.getClinic().getPetList().add(pet);
                    }
                }
            }
        } catch(SQLException | NameException e) {
            throw new IllegalStateException("Can't load data from database", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Client addClient(int position, String name)
            throws ServiceException, NameException {
        final Client client = super.addClient(position, name);

        try (final PreparedStatement statement = this.connection.prepareStatement(ADD_CLIENT_SQL,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, client.getPosition() + 1);
            statement.setString(2, client.getName());
            statement.executeUpdate();

            try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1));
                }
            }
        } catch(SQLException e) {
            super.deleteClient(client);
            throw new ServiceException(String.format("Can't insert client into database: %s", e.getMessage()));
        }

        return client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Pet setClientPet(String name, String petType, String petName)
            throws ServiceException, NameException {
        final Client client = this.findClientByName(name);
        final Pet oldPet = client.getPet();
        final Pet pet = super.setClientPet(client, petType, petName);

        try (final PreparedStatement statement = this.connection.prepareStatement(SET_CLIENT_PET_SQL,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, client.getId());
            statement.setString(2, pet.getType());
            statement.setString(3, pet.getName());
            statement.executeUpdate();

            try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pet.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            if (oldPet == Pet.NONE) {
                super.deleteClientPet(client);
            } else {
                super.setClientPet(client, oldPet);
            }
            throw new ServiceException(String.format("Can't insert client's pet into database: %s", e.getMessage()));
        }

        return pet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void updateClientName(String name, String newName) throws ServiceException, NameException {
        final Client client = this.findClientByName(name);
        super.updateClientName(client, newName);

        try (final PreparedStatement statement = this.connection.prepareStatement(UPDATE_CLIENT_NAME_SQL)) {
            statement.setString(1, newName);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            super.updateClientName(client, name);
            throw new ServiceException(String.format("Can't update client's name in database: %s", e.getMessage()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void updateClientPetName(String name, String petName) throws ServiceException, NameException {
        final Client client = this.findClientByName(name);
        final String oldPetName = client.getPet().getName();
        super.updateClientPetName(client, petName);

        try (final PreparedStatement statement = this.connection.prepareStatement(UPDATE_CLIENT_PET_NAME_SQL)) {
            statement.setString(1, petName);
            statement.setInt(2, client.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            super.updateClientPetName(client, oldPetName);
            throw new ServiceException(String.format("Can't update client pet's name in database: %s", e.getMessage()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void deleteClientPet(String name) throws ServiceException {
        final Client client = this.findClientByName(name);
        final Pet oldPet = client.getPet();
        super.deleteClientPet(client);

        try (final PreparedStatement statement = this.connection.prepareStatement(DELETE_PET_SQL)) {
            statement.setInt(1, client.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            super.setClientPet(client, oldPet);
            throw new ServiceException(String.format("Can't delete client's pet from database: %s", e.getMessage()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void deleteClient(String name) throws ServiceException {
        final Client client = this.findClientByName(name);
        final Pet oldPet = client.getPet();
        super.deleteClient(client);

        try (final PreparedStatement statement = this.connection.prepareStatement(DELETE_CLIENT_SQL)) {
            statement.setInt(1, client.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            try {
                super.addClient(client.getPosition(), client);
                if (oldPet != Pet.NONE) {
                    client.setPet(Pet.NONE);
                    super.setClientPet(client, oldPet);
                }
            } catch (NameException e1) {
                throw new IllegalStateException(e1);
            }
            throw new ServiceException(String.format("Can't delete client from database: %s", e.getMessage()));
        }
    }
}
