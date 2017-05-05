package ru.lightstar.clinic.form;

/**
 * Form used in update client request.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class UpdateClientForm {

    /**
     * Client's name.
     */
    private String name = "";

    /**
     * Client's new name.
     */
    private String newName = "";

    /**
     * Client's new role name.
     */
    private String newRole = "";

    /**
     * Client's new email.
     */
    private String newEmail = "";

    /**
     * Client's new phone.
     */
    private String newPhone = "";

    /**
     * Get client's name.
     * @return client's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set client's name.
     * @param name client's name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get client's new name.
     * @return client's new name.
     */
    public String getNewName() {
        return this.newName;
    }

    /**
     * Set client's new name.
     *
     * @param newName client's new name.
     */
    public void setNewName(final String newName) {
        this.newName = newName;
    }

    /**
     * Get client's new role name.
     *
     * @return client's new role name.
     */
    public String getNewRole() {
        return this.newRole;
    }

    /**
     * Set client's new role name.
     *
     * @param newRole client's new role name.
     */
    public void setNewRole(final String newRole) {
        this.newRole = newRole;
    }

    /**
     * Get client's new email.
     *
     * @return client's new email.
     */
    public String getNewEmail() {
        return this.newEmail;
    }

    /**
     * Set client's new email.
     *
     * @param newEmail client's new email.
     */
    public void setNewEmail(final String newEmail) {
        this.newEmail = newEmail;
    }

    /**
     * Get client's new phone.
     *
     * @return client's new phone.
     */
    public String getNewPhone() {
        return this.newPhone;
    }

    /**
     * Set client's new phone.
     *
     * @param newPhone client's new phone.
     */
    public void setNewPhone(final String newPhone) {
        this.newPhone = newPhone;
    }
}
