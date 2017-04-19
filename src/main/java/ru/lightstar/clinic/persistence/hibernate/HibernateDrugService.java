package ru.lightstar.clinic.persistence.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.lightstar.clinic.Clinic;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.PersistentDrugService;

import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;
import java.util.List;

/**
 * Database-aware <code>DrugService</code> implementation which uses hibernate.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class HibernateDrugService extends PersistentDrugService {

    /**
     * Hibernate's session factory.
     */
    private final SessionFactory sessionFactory;

    /**
     * Constructs <code>HibernateDrugService</code> object.
     */
    public HibernateDrugService(final Clinic clinic, final ServletContext context) {
        super(clinic);
        this.sessionFactory = (SessionFactory) context.getAttribute("sessionFactory");
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized void loadDrugs() {
        try (final Session session = this.sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                final List<Drug> list = session.createQuery("from Drug").list();
                for (final Drug drug : list) {
                    super.addDrug(drug);
                }
                transaction.commit();
            } catch (HibernateException | ServiceException e){
                transaction.rollback();
                throw new IllegalStateException("Can't load data from database", e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Drug addDrug(final String name) throws ServiceException {
        final Drug drug = super.addDrug(name);

        try (final Session session = this.sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                session.save(drug);
                transaction.commit();
            } catch (PersistenceException e) {
                super.takeDrug(drug);
                throw new ServiceException(String.format("Can't insert drug into database: %s", e.getMessage()));
            }
        }

        return drug;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Drug takeDrug(final String name) throws ServiceException {
        final Drug drug = super.takeDrug(name);

        try (final Session session = this.sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                session.delete(drug);
                transaction.commit();
            } catch (PersistenceException e) {
                super.addDrug(drug);
                throw new ServiceException(String.format("Can't remove drug from database: %s", e.getMessage()));
            }
        }

        return drug;
    }
}
