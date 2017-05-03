package ru.lightstar.clinic.pet;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Class used for persistence of pet's sex using hibernate.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class SexType implements UserType {

    /**
     * SQL types mapped by this type.
     */
    private static final int[] SQL_TYPES = {Types.OTHER};

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class returnedClass() {
        return Sex.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object value1, final Object value2)
            throws HibernateException {
        return value1 == value2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode(final Object value)
            throws HibernateException {
        return value.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object nullSafeGet(final ResultSet resultSet, final String[] names,
                              final SessionImplementor sessionImplementor,
                              final Object owner)
            throws HibernateException, SQLException {
        final String value = resultSet.getString(names[0]);
        if (resultSet.wasNull()) {
            return null;
        }
        return value.equals("m") ? Sex.M : Sex.F;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nullSafeSet(final PreparedStatement statement, final Object value, final int index,
                            final SessionImplementor sessionImplementor)
            throws HibernateException, SQLException {
        if (value == null) {
            statement.setNull(index, Types.OTHER);
        } else {
            statement.setString(index, value == Sex.M ? "m" : "f");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object deepCopy(final Object value)
            throws HibernateException {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMutable() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Serializable disassemble(final Object value)
            throws HibernateException {
        return (Serializable) value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object assemble(final Serializable cached, final Object owner)
            throws HibernateException {
        return cached;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object replace(final Object original, final Object target, final Object owner)
            throws HibernateException {
        return original;
    }
}
