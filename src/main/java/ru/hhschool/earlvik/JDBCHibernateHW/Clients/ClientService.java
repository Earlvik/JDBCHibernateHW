package ru.hhschool.earlvik.JDBCHibernateHW.Clients;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.Taxi;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiDAO;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiId;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiService;


import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Created by Earlviktor on 21.01.2015.
 */
public class ClientService {
        private final SessionFactory sessionFactory;
        private final ClientDAO clientDAO;
    
        public ClientService(final SessionFactory sessionFactory, final ClientDAO clientDAO){
            this.sessionFactory = sessionFactory;
            this.clientDAO = clientDAO;
        }
    
    public void insert(final Client client){
        inTransaction(() -> clientDAO.insert(client));
    }

    public Optional<Client> get(final ClientId clientId){
        return inTransaction(()->clientDAO.get(clientId));
    }

    public Set<Client> getAll(){
        return inTransaction(clientDAO::getAll);
    }

    public void update(final Client client) {
        inTransaction(() -> clientDAO.update(client));
    }

    public void delete(final ClientId clientId) {
        inTransaction(() -> clientDAO.delete(clientId));
    }

    public void callTaxi(final TaxiService taxiService, final ClientId clientId, final TaxiId taxiId){
        inTransaction(() ->{
            Optional<Taxi> taxiOptional = taxiService.get(taxiId);
            if(!taxiOptional.isPresent()) return;
            Taxi taxi = taxiOptional.get();
            if(!taxi.isAvailable()) return;
            Optional<Client> clientOptional = clientDAO.get(clientId);
            if(!clientOptional.isPresent()) return;
            taxi.setDrives(taxi.getDrives()+1);
            Client client = clientOptional.get();
            taxi.setAvailable(false);
            taxiService.update(taxi);
            client.setCarId(taxiId.getValue());
        });
    }

    private void inTransaction(final Runnable runnable) {
        inTransaction(()->{
            runnable.run();
            return null;
        });
    }

    private <T> T inTransaction(final Supplier<T> supplier){
        final Optional<Transaction> transaction = beginTransaction();
        try{
            final T result = supplier.get();
            transaction.ifPresent(Transaction::commit);
            return result;
        }catch (RuntimeException e){
            transaction.ifPresent(Transaction::rollback);
            throw e;
        }
    }

    private Optional<Transaction> beginTransaction() {
        final Transaction transaction = sessionFactory.getCurrentSession().getTransaction();
        if(!transaction.isActive()){
            transaction.begin();
            return Optional.of(transaction);
        }
        return Optional.empty();
    }
}
