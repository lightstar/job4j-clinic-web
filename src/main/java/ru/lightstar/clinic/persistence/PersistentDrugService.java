package ru.lightstar.clinic.persistence;

import ru.lightstar.clinic.Clinic;
import ru.lightstar.clinic.DrugService;

/**
 * Database-aware <code>DrugService</code> implementation.
 *
 * @author LightStar
 * @since 0.0.1
 */
public abstract class PersistentDrugService extends DrugService {

    /**
     * Constructs <code>PersistentDrugService</code> object.
     */
    public PersistentDrugService(final Clinic clinic) {
        super(clinic);
    }
}
