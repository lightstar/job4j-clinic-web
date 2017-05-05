package ru.lightstar.clinic.form;

/**
 * Form used in update client's pet request.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class UpdateClientPetForm {

    /**
     * Client's name.
     */
    private String name = "";

    /**
     * Pet's new name.
     */
    private String newName = "";

    /**
     * Pet's new age.
     */
    private int newAge = 0;

    /**
     * Pet's new sex.
     */
    private String newSex = "m";

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
     * Get pet's new name.
     *
     * @return pet's new name.
     */
    public String getNewName() {
        return this.newName;
    }

    /**
     * Set pet's new name.
     *
     * @param newName pet's new name.
     */
    public void setNewName(final String newName) {
        this.newName = newName;
    }

    /**
     * Get pet's new age.
     *
     * @return pet's new age.
     */
    public int getNewAge() {
        return this.newAge;
    }

    /**
     * Set pet's new age.
     *
     * @param newAge pet's new age.
     */
    public void setNewAge(final int newAge) {
        this.newAge = newAge;
    }

    /**
     * Get pet's new sex.
     *
     * @return pet's new sex.
     */
    public String getNewSex() {
        return this.newSex;
    }

    /**
     * Set pet's new sex.
     * @param newSex pet's new sex.
     */
    public void setNewSex(final String newSex) {
        this.newSex = newSex;
    }
}
