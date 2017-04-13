package ru.lightstar.clinic.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Jdbc settings.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class JdbcSettings {

    /**
     * Inner properties object.
     */
    private final Properties properties;

    /**
     * Constructs <code>JdbcSettings</code> object and loads properties.
     */
    public JdbcSettings() {
        this(new Properties(), JdbcSettings.class.getResourceAsStream("/jdbc.properties"));
    }

    /**
     * Constructs <code>JdbcSettings</code> object from given <code>Properties</code>
     * and <code>InputStream</code> objects.
     * Used in tests.
     *
     * @param properties pre-defined <code>Properties</code> object.
     * @param inputStream pre-defined <code>InputStream</code> object to load properties from.
     */
    JdbcSettings(final Properties properties, final InputStream inputStream) {
        this.properties = properties;
        try {
            this.properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Get property with given name.
     *
     * @param name property's name.
     * @return property's value.
     */
    public String value(final String name) {
        return this.properties.getProperty(name,"");
    }
}
