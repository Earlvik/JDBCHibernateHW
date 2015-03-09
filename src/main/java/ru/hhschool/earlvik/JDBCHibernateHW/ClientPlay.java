package ru.hhschool.earlvik.JDBCHibernateHW;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.hibernate.SessionFactory;
import ru.hhschool.earlvik.JDBCHibernateHW.Clients.*;
import ru.hhschool.earlvik.JDBCHibernateHW.Settings.Settings;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.*;

import java.sql.SQLException;

import static ru.hhschool.earlvik.JDBCHibernateHW.Settings.DataSourceUtils.mysqlDataSource;
import static ru.hhschool.earlvik.JDBCHibernateHW.Settings.SettingsUtils.loadSettings;

/**
 *  on 21.01.2015.
 */
public class ClientPlay {

    public static void main(String[] args) throws SQLException {
        Injector injector = Guice.createInjector(new TaxiModule(), new ClientModule());
        TaxiDAO taxiDAO = injector.getInstance(TaxiDAO.class);
        ClientDAO clientDAO = injector.getInstance(ClientDAO.class);
        final TaxiService taxiService = new TaxiService(taxiDAO);
        SessionFactory sessionFactory = injector.getInstance(SessionFactory.class);

        try {

            final ClientService clientService = new ClientService(sessionFactory, clientDAO);

            final Client client = new Client("Head", "Hunterz");
            System.out.println("persisting " + client);
            clientService.insert(client);
            System.out.println("clients in db: " + clientService.getAll());
            System.out.println();

            System.out.println("changing first name to 'Tail");
            client.setFirstName("Tail");
            clientService.update(client);
            System.out.println("users in db: " + clientService.getAll());
            System.out.println();

            final Taxi taxiAvailible = Taxi.create("Gordon", "Mini Cooper", true);
            final Taxi taxiNotAvailible = Taxi.create("Jimbo", "Pontiac", false);
            taxiService.insert(taxiAvailible);
            taxiService.insert(taxiNotAvailible);

            System.out.println("users in taxi db: " + taxiService.getAll());
            System.out.println();

            clientService.callTaxi(taxiService, client.getId(), taxiAvailible.getId());
            System.out.println("Called available taxi, now have these clients: " + clientService.getAll());
            System.out.println(" And these taxis: " + taxiService.getAll());
            System.out.println();

            clientService.callTaxi(taxiService, client.getId(), taxiNotAvailible.getId());
            System.out.println("Called not available taxi, now have this clients: " + clientService.getAll());
            System.out.println(" And these taxis: " + taxiService.getAll());
            System.out.println();


            System.out.println("deleting " + client);
            clientService.delete(client.getId());
            System.out.println("users in db: " + clientService.getAll());

        } finally {
            sessionFactory.close();
        }
    }




}
