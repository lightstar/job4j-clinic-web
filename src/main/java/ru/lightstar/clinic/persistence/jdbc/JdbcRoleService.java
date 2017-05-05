package ru.lightstar.clinic.persistence.jdbc;

import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.persistence.RoleService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service operating on roles which uses jdbc.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcRoleService extends JdbcService implements RoleService {

    /**
     * SQL used to get all roles from database.
     */
    public static final String ALL_ROLES_SQL =
            "SELECT * FROM role";

    /**
     * SQL used to get role by name from database.
     */
    public static final String ROLE_BY_NAME_SQL =
            "SELECT * FROM role WHERE name = ?";

    /**
     * SQL used to add new role to database.
     */
    public static final String ADD_ROLE_SQL =
            "INSERT INTO role (name) VALUES (?)";

    /**
     * SQL used to delete role from database.
     */
    public static final String DELETE_ROLE_SQL =
            "DELETE FROM role WHERE name = ?";

    /**
     * SQL used to get client by role's name from database.
     */
    public static final String CLIENT_BY_ROLE_SQL =
            "SELECT client.id FROM client " +
                    "INNER JOIN role ON client.role_id = role.id " +
                    "WHERE role.name = ? LIMIT 1";

    /**
     * Constructs <code>JdbcRoleService</code> object.
     *
     * @param connection jdbc connection.
     */
    public JdbcRoleService(final Connection connection) {
        super(connection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<Role> getAllRoles() {
        final List<Role> roles = new ArrayList<>();

        try (final Statement statement = this.connection.createStatement()) {
            try (final ResultSet rs = statement.executeQuery(ALL_ROLES_SQL)) {
                while (rs.next()) {
                    final int id = rs.getInt("id");
                    final String name = rs.getString("name");

                    final Role role = new Role(name);
                    role.setId(id);
                    roles.add(role);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(String.format("Can't get data from database: %s", e.getMessage()));
        }

        return roles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Role getRoleByName(final String name) throws ServiceException {
        final Role role = new Role();

        try (final PreparedStatement statement = this.connection.prepareStatement(ROLE_BY_NAME_SQL)) {
            statement.setString(1, name);
            try (final ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    role.setId(rs.getInt("id"));
                    role.setName(rs.getString("name"));
                } else {
                    throw new ServiceException("Role doesn't exists");
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(String.format("Can't get data from database: %s", e.getMessage()));
        }

        return role;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addRole(final String name) throws ServiceException {
        if (name.isEmpty()) {
            throw new ServiceException("Name is empty");
        }

        ServiceException expectedException = null;
        try {
            this.getRoleByName(name);
        } catch (ServiceException e) {
            expectedException = e;
        }

        if (expectedException == null) {
            throw new ServiceException("Role already exists");
        }

        try (final PreparedStatement statement = this.connection.prepareStatement(ADD_ROLE_SQL)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(String.format("Can't insert role into database: %s", e.getMessage()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void deleteRole(final String name) throws ServiceException {
        if (this.isRoleBusy(name)) {
            throw new ServiceException("Some client has this role");
        }

        try (final PreparedStatement statement = this.connection.prepareStatement(DELETE_ROLE_SQL)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(String.format("Can't delete role from database: %s", e.getMessage()));
        }
    }

    /**
     * Check if some client has given role.
     *
     * @param name role's name.
     * @return <code>true</code> if some client has given role and <code>false</code> otherwise.
     */
    private boolean isRoleBusy(final String name) {
        try (final PreparedStatement statement = this.connection.prepareStatement(CLIENT_BY_ROLE_SQL)) {
            statement.setString(1, name);
            try (final ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(String.format("Can't get data from database: %s", e.getMessage()));
        }
    }
}
