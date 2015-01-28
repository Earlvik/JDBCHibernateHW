package ru.hhschool.earlvik.JDBCHibernateHW;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.hibernate.SessionFactory;
import ru.hhschool.earlvik.JDBCHibernateHW.Clients.*;
import ru.hhschool.earlvik.JDBCHibernateHW.Settings.Settings;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.Taxi;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiService;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiSpringJDBCDAO;

import java.sql.SQLException;

import static ru.hhschool.earlvik.JDBCHibernateHW.Settings.DataSourceUtils.mysqlDataSource;
import static ru.hhschool.earlvik.JDBCHibernateHW.Settings.SettingsUtils.loadSettings;

/**
 * Created by Earlviktor on 21.01.2015.
 */
public class ClientPlay {

    public static void main(String[] args) throws SQLException {
        final SessionFactory sessionFactory = getSessionFactory();
        final Settings settings = loadSettings();
        final MysqlDataSource mysqlDataSource = mysqlDataSource(settings.database.url, settings.database.user, settings.database.password);
        final TaxiService taxiService = new TaxiService(new TaxiSpringJDBCDAO(mysqlDataSource));

        try {

            final ClientService clientService = getClientService(sessionFactory);

            final Client client = new Client("Head", "Hunterz",null);
            System.out.println("persisting " + client);
            clientService.insert(client);
            System.out.println("clients in db: " + clientService.getAll());
            System.out.println();

            System.out.println("changing first name to 'Tail");
            client.setFirstName("Tail");
            clientService.update(client);
            System.out.println("users in db: " + clientService.getAll());
            System.out.println();

            final Taxi taxiAvailible = Taxi.create("Gordon", "Mini Cooper",true);
            final Taxi taxiNotAvailible = Taxi.create("Jimbo","Pontiac",false);
            taxiService.insert(taxiAvailible);
            taxiService.insert(taxiNotAvailible);

            System.out.println("users in taxi db: " + taxiService.getAll());
            System.out.println();

            clientService.callTaxi(taxiService,new ClientId(client.getId()),taxiAvailible.getId());
            System.out.println("Called available taxi, now have these clients: " +clientService.getAll());
            System.out.println(" And these taxis: "+taxiService.getAll());
            System.out.println();

            clientService.callTaxi(taxiService, new ClientId(client.getId()),taxiNotAvailible.getId());
            System.out.println("Called not available taxi, now have this clients: " +clientService.getAll());
            System.out.println(" And these taxis: "+taxiService.getAll());
            System.out.println();


            System.out.println("deleting " + client);
            clientService.delete(new ClientId(client.getId()));
            System.out.println("users in db: " + clientService.getAll());

        } finally {
            sessionFactory.close();
        }
    }

    private static ClientService getClientService(SessionFactory sessionFactory) {
        final ClientDAO clientDAO = new ClientHibernateDAO(sessionFactory);
        return new ClientService(sessionFactory,clientDAO);
    }

    private static SessionFactory getSessionFactory() {
        return HibernateConfig.prod().buildSessionFactory();
    }


}
