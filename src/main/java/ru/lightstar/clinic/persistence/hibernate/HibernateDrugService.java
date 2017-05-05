package ru.lightstar.clinic.persistence.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.NonTransientDataAccessResourceException;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lightstar.clinic.Clinic;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.PersistentDrugService;

import java.util.List;

/**
 * Database-aware <code>DrugService</code> implementation which uses hibernate.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Service
public class HibernateDrugService extends PersistentDrugService {

    /**
     * Spring's hibernate template.
     */
    private final HibernateTemplate hibernateTemplate;

    /**
     * Constructs <code>HibernateDrugService</code> object.
     *
     * @param clinic clinic object.
     * @param hibernateTemplate spring's hibernate template.
     */
    @Autowired
    public HibernateDrugService(final Clinic clinic, final HibernateTemplate hibernateTemplate) {
        super(clinic);
        this.hibernateTemplate = hibernateTemplate;
        this.loadDrugs();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public synchronized Drug addDrug(final String name) throws ServiceException {
        final Drug drug = super.addDrug(name);

        try {
            this.hibernateTemplate.save(drug);
        } catch (DataAccessException e) {
            super.takeDrug(drug);
            throw e;
        }

        return drug;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public synchronized Drug takeDrug(final String name) throws ServiceException {
        final Drug drug = super.takeDrug(name);

        try {
            this.hibernateTemplate.delete(drug);
        } catch (DataAccessException e) {
            super.addDrug(drug);
            throw e;
        }

        return drug;
    }

    /**
     * Load all data from database to inner clinic object.
     */
    @SuppressWarnings("unchecked")
    private synchronized void loadDrugs() {
        try {
            final List<Drug> drugs = (List<Drug>) this.hibernateTemplate.find("from Drug");
            for (final Drug drug : drugs) {
                super.addDrug(drug);
            }
        } catch (ServiceException e) {
            throw new NonTransientDataAccessResourceException("Wrong drug data in database", e);
        }
    }
}
