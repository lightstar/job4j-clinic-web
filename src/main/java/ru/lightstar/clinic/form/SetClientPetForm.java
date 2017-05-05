package ru.lightstar.clinic.form;

/**
 * Form used in set client's pet request.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class SetClientPetForm {

    /**
     * Client's name.
     */
    private String name = "";

    /**
     * Pet's type.
     */
    private String petType = "";

    /**
     * Pet's name.
     */
    private String petName = "";

    /**
     * Pet's age.
     */
    private int petAge = 0;

    /**
     * Pet's sex.
     */
    private String petSex = "m";

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
     * Get pet's type.
     *
     * @return pet's type.
     */
    public String getPetType() {
        return this.petType;
    }

    /**
     * Set pet's type.
     *
     * @param petType pet's type.
     */
    public void setPetType(final String petType) {
        this.petType = petType;
    }

    /**
     * Get pet's name.
     *
     * @return pet's name.
     */
    public String getPetName() {
        return this.petName;
    }

    /**
     * Set pet's name.
     *
     * @param petName pet's name.
     */
    public void setPetName(final String petName) {
        this.petName = petName;
    }

    /**
     * Get pet's age.
     *
     * @return pet's age.
     */
    public int getPetAge() {
        return this.petAge;
    }

    /**
     * Set pet's age.
     *
     * @param petAge pet's age.
     */
    public void setPetAge(final int petAge) {
        this.petAge = petAge;
    }

    /**
     * Get pet's sex.
     *
     * @return pet's sex.
     */
    public String getPetSex() {
        return this.petSex;
    }

    /**
     * Set pet's sex.
     *
     * @param petSex pet's sex.
     */
    public void setPetSex(final String petSex) {
        this.petSex = petSex;
    }
}
