package ru.hhschool.earlvik.JDBCHibernateHW.Clients;

import java.util.Optional;
import java.util.Set;

public interface ClientDAO {

    public void insert(Client client);

    public Optional<Client> get(ClientId clientId);

    public Set<Client> getAll();

    public void update(Client client);

    public void delete(ClientId clientId);
}
