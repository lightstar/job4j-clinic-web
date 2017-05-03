package ru.lightstar.clinic.persistence.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized void loadDrugs() {
        try {
            final List<Drug> drugs = (List<Drug>) this.hibernateTemplate.find("from Drug");
            for (final Drug drug : drugs) {
                super.addDrug(drug);
            }
        } catch (DataAccessException | ServiceException e){
            throw new IllegalStateException("Can't load data from database", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = {ServiceException.class})
    @Override
    public synchronized Drug addDrug(final String name) throws ServiceException {
        final Drug drug = super.addDrug(name);

        try {
            this.hibernateTemplate.save(drug);
        } catch (DataAccessException e) {
            super.takeDrug(drug);
            throw new ServiceException(String.format("Can't insert drug into database: %s", e.getMessage()));
        }

        return drug;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = {ServiceException.class})
    @Override
    public synchronized Drug takeDrug(final String name) throws ServiceException {
        final Drug drug = super.takeDrug(name);

        try {
            this.hibernateTemplate.delete(drug);
        } catch (DataAccessException e) {
            super.addDrug(drug);
            throw new ServiceException(String.format("Can't remove drug from database: %s", e.getMessage()));
        }

        return drug;
    }
}
