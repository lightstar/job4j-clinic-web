package ru.lightstar.clinic;

import org.mockito.Mockito;
import ru.lightstar.clinic.jdbc.JdbcClinicService;

import java.sql.*;

/**
 * Helper object used to get mocked jdbc connection with some test data.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcConnectionMocker extends Mockito {

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
     * Mocking load clinic query.
     *
     * @param connection mocked connection.
     * @throws SQLException shouldn't be thrown.
     */
    private void mockLoadClinicQuery(final Connection connection) throws SQLException {
        final ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("id")).thenReturn(1).thenReturn(2).thenReturn(3);
        when(resultSet.getInt("position")).thenReturn(2).thenReturn(5).thenReturn(6);
        when(resultSet.getString("name")).thenReturn("Vasya").thenReturn("Masha").thenReturn("Vova");
        when(resultSet.getString("email")).thenReturn("vasya@mail.ru").thenReturn("masha@mail.ru")
                .thenReturn("vova@mail.ru");
        when(resultSet.getString("phone")).thenReturn("22222").thenReturn("123456").thenReturn("55555");
        when(resultSet.getInt("petId")).thenReturn(1).thenReturn(2).thenReturn(0);
        when(resultSet.getString("petType")).thenReturn("dog").thenReturn("cat").thenReturn(null);
        when(resultSet.getString("petName")).thenReturn("Bobik").thenReturn("Murka").thenReturn(null);
        when(resultSet.getInt("petAge")).thenReturn(10).thenReturn(6).thenReturn(0);
        when(resultSet.getString("petSex")).thenReturn("m").thenReturn("f").thenReturn(null);

        final Statement statement = mock(Statement.class);
        when(statement.executeQuery(JdbcClinicService.LOAD_SQL)).thenReturn(resultSet);

        when(connection.createStatement()).thenReturn(statement);
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
}
