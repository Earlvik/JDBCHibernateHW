package ru.hhschool.earlvik.JDBCHibernateHW;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.hhschool.earlvik.JDBCHibernateHW.Clients.*;
import ru.hhschool.earlvik.JDBCHibernateHW.Settings.Settings;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.Taxi;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiService;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiSpringJDBCDAO;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import static ru.hhschool.earlvik.JDBCHibernateHW.Settings.DataSourceUtils.mysqlDataSource;
import static ru.hhschool.earlvik.JDBCHibernateHW.Settings.SettingsUtils.loadSettings;

/**
 * Created by Earlviktor on 28.01.2015.
 */
public class ClientTest {

    private TaxiService taxiService;
    private ClientService clientService;

    private static SessionFactory getSessionFactory() {
        return HibernateConfig.prod().buildSessionFactory();
    }

    private static ClientService getClientService(SessionFactory sessionFactory) {
        final ClientDAO clientDAO = new ClientHibernateDAO(sessionFactory);
        return new ClientService(sessionFactory,clientDAO);
    }

    @Before
    public void prepare() throws SQLException {
        final SessionFactory sessionFactory = getSessionFactory();
        final Settings settings = loadSettings();
        final MysqlDataSource mysqlDataSource = mysqlDataSource(settings.database.url, settings.database.user, settings.database.password);
        taxiService = new TaxiService(new TaxiSpringJDBCDAO(mysqlDataSource));
        clientService = getClientService(sessionFactory);
    }

    @Test
    public void insertNonExistingUserGetHimAndCheckParametersTest(){
        Client client = new Client("Jonh","Doe", null);
        clientService.insert(client);
        Optional<Client> returned = clientService.get(new ClientId(client.getId()));
        Assert.assertTrue("Inserted user was not found in database: " + client, returned.isPresent());
        Client returnedClient = returned.get();
        Assert.assertTrue("Returned taxi: " + returnedClient + " was not equal to " + client,
                client.equals(returnedClient));
    }


    @Test
    public void insertSeveralClientsGetAllTest(){
        Client bob = new Client("Bob","Doe", null);
        Client steve = new Client("Steve","Jenkins", null);

        clientService.insert(bob);
        clientService.insert(steve);

        Set<Client> clients = clientService.getAll();
        Assert.assertNotNull("The returned set of results was null",clients);
        Assert.assertTrue("No Bob returned, only "+clients,clients.stream().anyMatch(x -> x.equals(bob)));
        Assert.assertTrue("No Steve returned, only "+clients,clients.stream().anyMatch(x -> x.equals(steve)));
    }

    @Test
    public void insertAndUpdateClientGetUpdatedVersionTest(){
        Client client = new Client("Jonh","Doe", null);
        clientService.insert(client);
        ClientId id = new ClientId(client.getId());
        client.setFirstName("Steve");
        clientService.update(client);
        Optional<Client> returned = clientService.get(id);
        Assert.assertTrue(returned.isPresent());
        Assert.assertTrue(returned.get().equals(client));
    }


    @Test
    public void insertAndDeleteClientGetNothingTest(){
        Client client = new Client("Bob","Doe", null);
        clientService.insert(client);
        ClientId id = new ClientId(client.getId());
        clientService.delete(id);
        Optional<Client> returned = clientService.get(id);
        Assert.assertFalse("Bob is still found, though deleted", returned.isPresent());
        Set<Client> clients = clientService.getAll();
        Assert.assertTrue("Bob is still found in all clients", clients.stream().noneMatch(x->x.equals(client)));
    }

    @Test
    public void insertClientAndTaxiCallAvailableTaxiForUser(){
        Client client = new Client("Jack","Peterson",null);
        Taxi taxi = Taxi.create("David", "Honda",true);
        clientService.insert(client);
        taxiService.insert(taxi);
        clientService.callTaxi(taxiService,new ClientId(client.getId()),taxi.getId());
        Optional<Client> returned = clientService.get(new ClientId(client.getId()));
        Assert.assertTrue(" No client returned",returned.isPresent());
        Client returnedClient = returned.get();
        Assert.assertTrue("Wrong car id. Had to be "+taxi.getId().getValue()+" but got "+
                returnedClient.getId(),returnedClient.getCarId() == taxi.getId().getValue());
    }

}
