package ru.hhschool.earlvik.JDBCHibernateHW.Clients;

import com.google.common.collect.ImmutableSet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ClientHibernateDAO implements ClientDAO {

    private final SessionFactory sessionFactory;

    public ClientHibernateDAO(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void insert(Client client) {
        if (client.getId() != null) {
            throw new IllegalArgumentException("can not insert " + client + " with assigned id");
        }
        session().save(client);
    }

    @Override
    public Optional<Client> get(ClientId clientId) {
        final Client client = (Client) session().get(Client.class, clientId.getValue());
        return Optional.ofNullable(client);
    }

    @Override
    public Set<Client> getAll() {
        final Criteria criteria = session().createCriteria(Client.class);
        final List<Client> clients = criteria.list();
        return ImmutableSet.copyOf(clients);
    }

    @Override
    public void update(Client client) {
        session().update(client);
    }

    @Override
    public void delete(ClientId clientId) {
        session().createQuery("DELETE Client WHERE client_id = :id")
                .setInteger("id", clientId.getValue())
                .executeUpdate();

    }
}
