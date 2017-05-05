package ru.lightstar.clinic.form;

/**
 * Form used to filter list of clients in clinic.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class FilterForm {

    /**
     * Filter's type value.
     */
    private String filterType = "";

    /**
     * Filter's name value.
     */
    private String filterName = "";

    /**
     * Get filter's type value.
     *
     * @return filter's type value.
     */
    public String getFilterType() {
        return this.filterType;
    }

    /**
     * Set filter's type value.
     *
     * @param filterType filter's type value.
     */
    public void setFilterType(final String filterType) {
        this.filterType = filterType;
    }

    /**
     * Get filter's name value.
     *
     * @return filter's name value.
     */
    public String getFilterName() {
        return this.filterName;
    }

    /**
     * Set filter's name value.
     *
     * @param filterName filter's name value.
     */
    public void setFilterName(final String filterName) {
        this.filterName = filterName;
    }

    /**
     * Check if filter is empty.
     *
     * @return <code>true</code> if filter is empty (i.e. efficiently not present)
     * and <code>false</code> - otherwise.
     */
    public boolean isEmpty() {
        return this.filterName.isEmpty();
    }
}