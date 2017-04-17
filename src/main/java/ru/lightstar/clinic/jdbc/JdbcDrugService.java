package ru.lightstar.clinic.jdbc;

import ru.lightstar.clinic.Clinic;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.exception.ServiceException;

import javax.servlet.ServletContext;
import java.sql.*;

/**
 * Database-aware <code>DrugService</code> implementation.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcDrugService extends DrugService {

    /**
     * SQL used to load all drug data from database.
     */
    public static final String LOAD_SQL =
            "SELECT drug.id, drug.name FROM drug";

    /**
     * SQL used to add new drug to database.
     */
    public static final String ADD_SQL =
            "INSERT INTO drug (name, danger) VALUES (?,?)";

    /**
     * SQL used to delete drug from database.
     */
    public static final String DELETE_SQL =
            "DELETE FROM drug WHERE id = ?";

    /**
     * Jdbc connection used by this service.
     */
    private final Connection connection;

    /**
     * Constructs <code>JdbcClinicService</code> object.
     */
    public JdbcDrugService(final Clinic clinic, final ServletContext context) {
        super(clinic);
        this.connection = (Connection) context.getAttribute("jdbcConnection");
    }

    /**
     * Load all drug data from database to inner clinic object.
     */
    public synchronized void loadDrugs() {
        try (final Statement statement = this.connection.createStatement()) {
            try (final ResultSet rs = statement.executeQuery(LOAD_SQL)) {
                while (rs.next()) {
                    final int id = rs.getInt("id");
                    final String name = rs.getString("name");
                    final Drug drug = super.addDrug(name);
                    drug.setId(id);
                }
            }
        } catch(SQLException | ServiceException e) {
            throw new IllegalStateException("Can't load data from database", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Drug addDrug(final String name) throws ServiceException {
        final Drug drug = super.addDrug(name);

        try (final PreparedStatement statement = this.connection.prepareStatement(ADD_SQL,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, drug.getName());
            statement.setInt(2, drug.getDangerLevel());
            statement.executeUpdate();

            try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    drug.setId(generatedKeys.getInt(1));
                }
            }
        } catch(SQLException e) {
            super.takeDrug(drug);
            throw new ServiceException(String.format("Can't insert drug into database: %s", e.getMessage()));
        }

        return drug;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Drug takeDrug(final String name) throws ServiceException {
        final Drug drug = super.takeDrug(name);

        try (final PreparedStatement statement = this.connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, drug.getId());
            statement.executeUpdate();
        } catch(SQLException e) {
            super.addDrug(drug);
            throw new ServiceException(String.format("Can't remove drug from database: %s", e.getMessage()));
        }

        return drug;
    }
}
