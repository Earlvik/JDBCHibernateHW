package ru.hhschool.earlvik.JDBCHibernateHW.Clients;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ClientHibernateDAO implements ClientDAO {

    protected Logger logger = LoggerFactory.getLogger(ClientDAO.class);


    private final SessionFactory sessionFactory;

    @Inject
    public ClientHibernateDAO(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void insert(Client client) {
        if (client.getId() != null) {
            logger.warn("Could not insert " + client + " with assigned id");
            throw new IllegalArgumentException("can not insert " + client + " with assigned id");
        }
        session().save(client);
        logger.info("Successfully inserted client: "+client.toString());
    }

    @Override
    public Optional<Client> get(ClientId clientId) {
        final Client client = (Client) session().get(Client.class, clientId.getValue());
        logger.info("Retrieved client: "+client.toString());
        return Optional.ofNullable(client);
    }

    @Override
    public Set<Client> getAll() {
        final Criteria criteria = session().createCriteria(Client.class);
        final List<Client> clients = criteria.list();
        logger.info("Retrieved all clients");
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
        logger.info("Deleted client with id "+clientId.getValue());

    }
}
