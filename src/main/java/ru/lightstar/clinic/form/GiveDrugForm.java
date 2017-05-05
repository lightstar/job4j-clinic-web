package ru.lightstar.clinic.form;

/**
 * Form used in give drug operation.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class GiveDrugForm {

    /**
     * Drug's name.
     */
    private String name = "";

    /**
     * Client's name.
     */
    private String clientName = "";

    /**
     * Get drug's name.
     *
     * @return drug's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set drug's name.
     *
     * @param name drug's name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get client's name.
     *
     * @return client's name.
     */
    public String getClientName() {
        return this.clientName;
    }

    /**
     * Set client's name.
     *
     * @param clientName client's name.
     */
    public void setClientName(final String clientName) {
        this.clientName = clientName;
    }
}
