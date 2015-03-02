package ru.hhschool.earlvik.JDBCHibernateHW.Taxis;

import java.util.Optional;
import java.util.Set;

/**
 *  on 21.01.2015.
 */
public interface TaxiDAO {

    void insert(Taxi taxi);

    Optional<Taxi> get(TaxiId taxiId);

    Set<Taxi> getAll();

    void update(Taxi taxi);

    void delete(TaxiId taxiId);

}
