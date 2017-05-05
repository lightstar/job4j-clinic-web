package ru.lightstar.clinic.form;

/**
 * Form used in add client request.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class AddClientForm {

    /**
     * Client's name.
     */
    private String name = "";

    /**
     * Client's role name.
     */
    private String role = "";

    /**
     * Client's position in clinic.
     */
    private int pos = 0;

    /**
     * Client's email.
     */
    private String email = "";

    /**
     * Client's phone.
     */
    private String phone = "";

    /**
     * Get client's name.
     *
     * @return client's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set client's name.
     *
     * @param name client's name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get client's role name.
     *
     * @return client's role name.
     */
    public String getRole() {
        return this.role;
    }

    /**
     * Set client's role name.
     *
     * @param role client's role name.
     */
    public void setRole(final String role) {
        this.role = role;
    }

    /**
     * Get client's position in clinic.
     *
     * @return client's position in clinic.
     */
    public int getPos() {
        return this.pos;
    }

    /**
     * Set client's position in clinic.
     *
     * @param pos client's position in clinic.
     */
    public void setPos(final int pos) {
        this.pos = pos;
    }

    /**
     * Get client's email.
     *
     * @return client's email.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Set client's email.
     *
     * @param email client's email.
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Get client's phone.
     *
     * @return client's phone.
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * Set client's phone.
     *
     * @param phone client's phone.
     */
    public void setPhone(final String phone) {
        this.phone = phone;
    }
}
