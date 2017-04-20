package ru.lightstar.clinic.pet;

import org.junit.Test;
import org.mockito.Mockito;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * <code>SexType</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class SexTypeTest extends Mockito {

    /**
     * Test correctness of <code>sqlTypes</code> method.
     */
    @Test
    public void whenSqlTypesThenResult() {
        assertThat(new SexType().sqlTypes(), is(new int[]{Types.OTHER}));
    }

    /**
     * Test correctness of <code>returnedClass</code> method.
     */
    @Test
    public void whenReturnedClassThenResult() {
        assertThat(new SexType().returnedClass(), is(equalTo(Sex.class)));
    }

    /**
     * Test correctness of <code>equals</code> method with identical parameters.
     */
    @Test
    public void whenEqualsThenResult() {
        assertThat(new SexType().equals(Sex.M, Sex.M), is(true));
    }

    /**
     * Test correctness of <code>equals</code> method with different parameters.
     */
    @Test
    public void whenNotEqualsThenResult() {
        assertThat(new SexType().equals(Sex.M, Sex.F), is(false));
    }

    /**
     * Test correctness of <code>hashCode</code> method.
     */
    @Test
    public void whenHashCodeThenResult() {
        assertThat(new SexType().hashCode(Sex.M), is(Sex.M.hashCode()));
    }

    /**
     * Test correctness of <code>nullSafeGet</code> method.
     */
    @Test
    public void whenNullSafeGetThenResult() throws SQLException {
        final ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString("sex")).thenReturn("m");
        when(resultSet.wasNull()).thenReturn(false);
        assertThat(new SexType().nullSafeGet(resultSet, new String[]{"sex"}, null, null),
                is(Sex.M));
    }

    /**
     * Test correctness of <code>nullSafeGet</code> method with null value.
     */
    @Test
    public void whenNullSafeGetNullThenResult() throws SQLException {
        final ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString("sex")).thenReturn(null);
        when(resultSet.wasNull()).thenReturn(true);
        assertThat(new SexType().nullSafeGet(resultSet, new String[]{"sex"}, null, null), nullValue());
    }

    /**
     * Test correctness of <code>nullSafeSet</code> method.
     */
    @Test
    public void whenNullSafeSetThenResult() throws SQLException {
        final PreparedStatement statement = mock(PreparedStatement.class);
        new SexType().nullSafeSet(statement, Sex.M, 0, null);
        verify(statement, times(1)).setString(0, "m");
    }

    /**
     * Test correctness of <code>nullSafeSet</code> method with null value.
     */
    @Test
    public void whenNullSafeSetNullThenResult() throws SQLException {
        final PreparedStatement statement = mock(PreparedStatement.class);
        new SexType().nullSafeSet(statement, null, 0, null);
        verify(statement, times(1)).setNull(0, Types.OTHER);
    }

    /**
     * Test correctness of <code>deepCopy</code> method.
     */
    @Test
    public void whenDeepCopyThenResult() {
        assertThat(new SexType().deepCopy(Sex.M), is(Sex.M));
    }

    /**
     * Test correctness of <code>isMutable</code> method.
     */
    @Test
    public void whenIsMutableThenFalse() {
        assertThat(new SexType().isMutable(), is(false));
    }

    /**
     * Test correctness of <code>disassemble</code> method.
     */
    @Test
    public void whenDisassembleThenResult() {
        assertThat(new SexType().disassemble(Sex.M), is(Sex.M));
    }

    /**
     * Test correctness of <code>assemble</code> method.
     */
    @Test
    public void whenAssembleThenResult() {
        assertThat(new SexType().assemble(Sex.M, null), is(Sex.M));
    }

    /**
     * Test correctness of <code>replace</code> method.
     */
    @Test
    public void whenReplaceThenResult() {
        assertThat(new SexType().replace(Sex.M, null, null), is(Sex.M));
    }
}