package ru.hhschool.earlvik.JDBCHibernateHW.Taxis;

import java.util.Optional;
import java.util.Set;

/**
 *
 */
public class TaxiService {

    private final TaxiDAO taxiDAO;

    public TaxiService(final TaxiDAO taxiDAO) {
        this.taxiDAO = taxiDAO;
    }

    public void insert(final Taxi taxi) {
        taxiDAO.insert(taxi);
    }

    public Optional<Taxi> get(final TaxiId taxiId) {
        return taxiDAO.get(taxiId);
    }

    public Set<Taxi> getAll() {
        return taxiDAO.getAll();
    }

    public void update(Taxi taxi) {
        taxiDAO.update(taxi);
    }

    public void delete(TaxiId taxiId) {
        taxiDAO.delete(taxiId);
    }

}
