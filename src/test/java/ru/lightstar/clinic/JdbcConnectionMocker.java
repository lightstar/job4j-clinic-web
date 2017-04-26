package ru.lightstar.clinic;

import org.mockito.Mockito;
import ru.lightstar.clinic.persistence.jdbc.JdbcClinicService;
import ru.lightstar.clinic.persistence.jdbc.JdbcDrugService;
import ru.lightstar.clinic.persistence.jdbc.JdbcRoleService;

import java.sql.*;

/**
 * Helper object used to get mocked jdbc connection with some test data.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcConnectionMocker extends Mockito {

    /**
     * Mocked result set for load clinic query.
     */
    private ResultSet loadClinicResultSet;

    /**
     * Mocked result set for load drugs query.
     */
    private ResultSet loadDrugsResultSet;

    /**
     * Mocked result set for get all roles query.
     */
    private ResultSet allRolesResultSet;

    /**
     * Mocked prepared statement for add client query.
     */
    private PreparedStatement addClientStatement;

    /**
     * Mocked result set for generated key for add client query.
     */
    private ResultSet generatedKeyForAddClientResultSet;

    /**
     * Mocked prepared statement for set client pet query.
     */
    private PreparedStatement setClientPetStatement;

    /**
     * Mocked result set for generated key for set client pet query.
     */
    private ResultSet generatedKeyForSetClientPetResultSet;

    /**
     * Mocked prepared statement for update client query.
     */
    private PreparedStatement updateClientStatement;

    /**
     * Mocked prepared statement for update client pet query.
     */
    private PreparedStatement updateClientPetStatement;

    /**
     * Mocked prepared statement for delete client pet query.
     */
    private PreparedStatement deleteClientPetStatement;

    /**
     * Mocked prepared statement for delete client query.
     */
    private PreparedStatement deleteClientStatement;

    /**
     * Mocked prepared statement for add drug query.
     */
    private PreparedStatement addDrugStatement;

    /**
     * Mocked result set for generated key for add drug query.
     */
    private ResultSet generatedKeyForAddDrugResultSet;

    /**
     * Mocked prepared statement for delete drug query.
     */
    private PreparedStatement deleteDrugStatement;

    /**
     * Mocked prepared statement for get role by name query.
     */
    private PreparedStatement roleByNameStatement;

    /**
     * Mocked result set for get role by name query.
     */
    private ResultSet roleByNameResultSet;

    /**
     * Mocked result set for checking if role is busy query.
     */
    private ResultSet roleBusyResultSet;

    /**
     * Mocked prepared statement for add role query.
     */
    private PreparedStatement addRoleStatement;

    /**
     * Mocked prepared statement for delete role query.
     */
    private PreparedStatement deleteRoleStatement;

    /**
     * Get mocked jdbc connection with test data.
     *
     * @return mocked jdbc connection.
     */
    public Connection getConnection() {
        final Connection connection = mock(Connection.class);

        try {
            this.mockLoadClinicQuery(connection);
            this.mockAddClientQuery(connection);
            this.mockSetClientPetQuery(connection);
            this.mockUpdateClientQuery(connection);
            this.mockUpdateClientPetQuery(connection);
            this.mockDeleteClientPetQuery(connection);
            this.mockDeleteClientQuery(connection);

            this.mockLoadDrugsQuery(connection);
            this.mockAddDrugQuery(connection);
            this.mockDeleteDrugQuery(connection);

            this.mockAllRolesQuery(connection);
            this.mockRoleByNameQuery(connection);
            this.mockRoleBusyQuery(connection);
            this.mockAddRoleQuery(connection);
            this.mockDeleteRoleQuery(connection);

            this.mockCreateStatement(connection);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return connection;
    }

    /**
     * Get mocked prepared statement for add client query.
     *
     * @return mocked prepared statement.
     */
    public PreparedStatement getAddClientStatement() {
        return this.addClientStatement;
    }

    /**
     * Get mocked result set for generated key for add client query.
     *
     * @return mocked result set.
     */
    public ResultSet getGeneratedKeyForAddClientResultSet() {
        return this.generatedKeyForAddClientResultSet;
    }

    /**
     * Get mocked prepared statement for set client pet query.
     *
     * @return mocked prepared statement.
     */
    public PreparedStatement getSetClientPetStatement() {
        return this.setClientPetStatement;
    }

    /**
     * Get mocked result set for generated key for set client pet query.
     *
     * @return mocked result set.
     */
    public ResultSet getGeneratedKeyForSetClientPetResultSet() {
        return this.generatedKeyForSetClientPetResultSet;
    }

    /**
     * Get mocked prepared statement for update client query.
     *
     * @return mocked prepared statement.
     */
    public PreparedStatement getUpdateClientStatement() {
        return this.updateClientStatement;
    }

    /**
     * Get mocked prepared statement for update client pet query.
     *
     * @return mocked prepared statement.
     */
    public PreparedStatement getUpdateClientPetStatement() {
        return this.updateClientPetStatement;
    }

    /**
     * Get mocked prepared statement for delete client pet query.
     *
     * @return mocked prepared statement.
     */
    public PreparedStatement getDeleteClientPetStatement() {
        return this.deleteClientPetStatement;
    }

    /**
     * Get mocked prepared statement for delete client query.
     *
     * @return mocked prepared statement.
     */
    public PreparedStatement getDeleteClientStatement() {
        return this.deleteClientStatement;
    }

    /**
     * Get mocked prepared statement for add drug query.
     *
     * @return mocked prepared statement.
     */
    public PreparedStatement getAddDrugStatement() {
        return this.addDrugStatement;
    }

    /**
     * Get mocked result set for generated key for add drug query.
     *
     * @return mocked result set.
     */
    public ResultSet getGeneratedKeyForAddDrugResultSet() {
        return this.generatedKeyForAddDrugResultSet;
    }

    /**
     * Get mocked prepared statement for delete drug query.
     *
     * @return mocked prepared statement.
     */
    public PreparedStatement getDeleteDrugStatement() {
        return this.deleteDrugStatement;
    }

    /**
     * Get mocked result set for get all roles query.
     *
     * @return mocked result set.
     */
    public ResultSet getAllRolesResultSet() {
        return this.allRolesResultSet;
    }

    /**
     * Get mocked prepared statement for get role by name query.
     *
     * @return mocked prepared statement.
     */
    public PreparedStatement getRoleByNameStatement() {
        return this.roleByNameStatement;
    }

    /**
     * Get mocked result set for get role by name query.
     *
     * @return mocked result set.
     */
    public ResultSet getRoleByNameResultSet() {
        return this.roleByNameResultSet;
    }

    /**
     * Get mocked prepared statement for add role query.
     *
     * @return mocked prepared statement.
     */
    public PreparedStatement getAddRoleStatement() {
        return this.addRoleStatement;
    }

    /**
     * Get mocked prepared statement for delete role query.
     *
     * @return mocked prepared statement.
     */
    public PreparedStatement getDeleteRoleStatement() {
        return this.deleteRoleStatement;
    }

    /**
     * Get mocked result set for checking if role is busy query.
     *
     * @return mocked result set.
     */
    public ResultSet getRoleBusyResultSet() {
        return this.roleBusyResultSet;
    }

    /**
     * Mocking <code>createStatement</code> method.
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockCreateStatement(final Connection connection) throws SQLException {
        final Statement statement = mock(Statement.class);
        when(statement.executeQuery(JdbcClinicService.LOAD_SQL)).thenReturn(this.loadClinicResultSet);
        when(statement.executeQuery(JdbcDrugService.LOAD_SQL)).thenReturn(this.loadDrugsResultSet);
        when(statement.executeQuery(JdbcRoleService.ALL_ROLES_SQL)).thenReturn(this.allRolesResultSet);

        when(connection.createStatement()).thenReturn(statement);
    }

    /**
     * Mocking load clinic query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockLoadClinicQuery(final Connection connection) throws SQLException {
        this.loadClinicResultSet = mock(ResultSet.class);
        when(this.loadClinicResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true)
                .thenReturn(false);
        when(this.loadClinicResultSet.getInt("id")).thenReturn(1).thenReturn(2)
                .thenReturn(3);
        when(this.loadClinicResultSet.getInt("position")).thenReturn(2).thenReturn(5)
                .thenReturn(6);
        when(this.loadClinicResultSet.getString("name")).thenReturn("Vasya").thenReturn("Masha")
                .thenReturn("Vova");
        when(this.loadClinicResultSet.getString("email")).thenReturn("vasya@mail.ru")
                .thenReturn("masha@mail.ru").thenReturn("vova@mail.ru");
        when(this.loadClinicResultSet.getString("phone")).thenReturn("22222").thenReturn("123456")
                .thenReturn("55555");
        when(this.loadClinicResultSet.getInt("roleId")).thenReturn(2).thenReturn(2)
                .thenReturn(1);
        when(this.loadClinicResultSet.getString("roleName")).thenReturn("client").thenReturn("client")
                .thenReturn("admin");
        when(this.loadClinicResultSet.getInt("petId")).thenReturn(1).thenReturn(2)
                .thenReturn(0);
        when(this.loadClinicResultSet.getString("petType")).thenReturn("dog").thenReturn("cat")
                .thenReturn(null);
        when(this.loadClinicResultSet.getString("petName")).thenReturn("Bobik").thenReturn("Murka")
                .thenReturn(null);
        when(this.loadClinicResultSet.getInt("petAge")).thenReturn(10).thenReturn(6).thenReturn(0);
        when(this.loadClinicResultSet.getString("petSex")).thenReturn("m").thenReturn("f")
                .thenReturn(null);
    }

    /**
     * Mocking add client query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockAddClientQuery(final Connection connection) throws SQLException {
        this.addClientStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcClinicService.ADD_CLIENT_SQL, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(this.addClientStatement);

        this.generatedKeyForAddClientResultSet = mock(ResultSet.class);
        when(this.generatedKeyForAddClientResultSet.next()).thenReturn(true).thenReturn(false);
        when(this.generatedKeyForAddClientResultSet.getInt(1)).thenReturn(4);

        when(this.addClientStatement.getGeneratedKeys()).thenReturn(this.generatedKeyForAddClientResultSet);
    }

    /**
     * Mocking set client pet query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockSetClientPetQuery(final Connection connection) throws SQLException {
        this.setClientPetStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcClinicService.SET_CLIENT_PET_SQL, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(this.setClientPetStatement);

        this.generatedKeyForSetClientPetResultSet = mock(ResultSet.class);
        when(this.generatedKeyForSetClientPetResultSet.next()).thenReturn(true).thenReturn(false);
        when(this.generatedKeyForSetClientPetResultSet.getInt(1)).thenReturn(5);

        when(this.setClientPetStatement.getGeneratedKeys()).thenReturn(this.generatedKeyForSetClientPetResultSet);
    }

    /**
     * Mocking update client query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockUpdateClientQuery(final Connection connection) throws SQLException {
        this.updateClientStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcClinicService.UPDATE_CLIENT_SQL))
                .thenReturn(this.updateClientStatement);
    }

    /**
     * Mocking update client pet query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockUpdateClientPetQuery(final Connection connection) throws SQLException {
        this.updateClientPetStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcClinicService.UPDATE_CLIENT_PET_SQL))
                .thenReturn(this.updateClientPetStatement);
    }

    /**
     * Mocking delete client pet query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockDeleteClientPetQuery(final Connection connection) throws SQLException {
        this.deleteClientPetStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcClinicService.DELETE_CLIENT_PET_SQL))
                .thenReturn(this.deleteClientPetStatement);
    }

    /**
     * Mocking delete client query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockDeleteClientQuery(final Connection connection) throws SQLException {
        this.deleteClientStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcClinicService.DELETE_CLIENT_SQL))
                .thenReturn(this.deleteClientStatement);
    }

    /**
     * Mocking load drugs query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockLoadDrugsQuery(final Connection connection) throws SQLException {
        this.loadDrugsResultSet = mock(ResultSet.class);
        when(this.loadDrugsResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true)
                .thenReturn(false);
        when(this.loadDrugsResultSet.getInt("id")).thenReturn(1).thenReturn(2)
                .thenReturn(3).thenReturn(4);
        when(this.loadDrugsResultSet.getString("name")).thenReturn("aspirin").thenReturn("aspirin")
                .thenReturn("glucose").thenReturn("aspirin");
    }

    /**
     * Mocking add drug query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockAddDrugQuery(final Connection connection) throws SQLException {
        this.addDrugStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcDrugService.ADD_SQL, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(this.addDrugStatement);

        this.generatedKeyForAddDrugResultSet = mock(ResultSet.class);
        when(this.generatedKeyForAddDrugResultSet.next()).thenReturn(true).thenReturn(false);
        when(this.generatedKeyForAddDrugResultSet.getInt(1)).thenReturn(6);

        when(this.addDrugStatement.getGeneratedKeys()).thenReturn(this.generatedKeyForAddDrugResultSet);
    }

    /**
     * Mocking delete drug query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockDeleteDrugQuery(final Connection connection) throws SQLException {
        this.deleteDrugStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcDrugService.DELETE_SQL))
                .thenReturn(this.deleteDrugStatement);
    }

    /**
     * Mocking get all roles query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockAllRolesQuery(final Connection connection) throws SQLException {
        this.allRolesResultSet = mock(ResultSet.class);
        when(this.allRolesResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(this.allRolesResultSet.getInt("id")).thenReturn(1).thenReturn(2);
        when(this.allRolesResultSet.getString("name")).thenReturn("admin").thenReturn("client");
    }

    /**
     * Mocking get role by name query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockRoleByNameQuery(final Connection connection) throws SQLException {
        this.roleByNameStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcRoleService.ROLE_BY_NAME_SQL)).thenReturn(this.roleByNameStatement);

        this.roleByNameResultSet = mock(ResultSet.class);
        when(this.roleByNameStatement.executeQuery()).thenReturn(this.roleByNameResultSet);

        when(this.roleByNameResultSet.next()).thenReturn(true).thenReturn(false);
        when(this.roleByNameResultSet.getInt("id")).thenReturn(2);
        when(this.roleByNameResultSet.getString("name")).thenReturn("client");
    }

    /**
     * Mocking check if role is busy query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockRoleBusyQuery(final Connection connection) throws SQLException {
        final PreparedStatement roleBusyStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcRoleService.CLIENT_BY_ROLE_SQL)).thenReturn(roleBusyStatement);

        this.roleBusyResultSet = mock(ResultSet.class);
        when(roleBusyStatement.executeQuery()).thenReturn(this.roleBusyResultSet);
    }

    /**
     * Mocking add role query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockAddRoleQuery(final Connection connection) throws SQLException {
        this.addRoleStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcRoleService.ADD_ROLE_SQL))
                .thenReturn(this.addRoleStatement);
    }

    /**
     * Mocking delete role query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockDeleteRoleQuery(final Connection connection) throws SQLException {
        this.deleteRoleStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(JdbcRoleService.DELETE_ROLE_SQL))
                .thenReturn(this.deleteRoleStatement);
    }
}
