package ru.lightstar.clinic.store;

import ru.lightstar.clinic.Clinic;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.io.DummyOutput;
import ru.lightstar.clinic.io.IteratorInput;

/**
 * Clinic cache for global use in all servlets.
 * It uses clinic with pre-defined size which clears on application restart.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ClinicCache {

    /**
     * Size of inner clinic.
     */
    private static final int CLINIC_SIZE = 10;

    /**
     * Global service instance with dummy input/output and pre-defined size.
     */
    private static final ClinicService SERVICE = new ClinicService(new IteratorInput(),
            new DummyOutput(), new Clinic(CLINIC_SIZE));

    /**
     * Get global clinic service instance with dummy input/output and pre-defined size.
     *
     * @return clinic service instance.
     */
    public static ClinicService getService() {
        return ClinicCache.SERVICE;
    }
}
