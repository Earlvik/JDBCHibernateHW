package ru.hhschool.earlvik.JDBCHibernateHW;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import ru.hhschool.earlvik.JDBCHibernateHW.Settings.Settings;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.Taxi;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiDAO;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiModule;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiSpringJDBCDAO;

import java.sql.SQLException;
import java.util.Optional;

import static ru.hhschool.earlvik.JDBCHibernateHW.Settings.SettingsUtils.loadSettings;
import static ru.hhschool.earlvik.JDBCHibernateHW.Settings.DataSourceUtils.mysqlDataSource;

/**
 *  on 21.01.2015.
 */
public class TaxiPlay {

    public static void main(String[] args) throws SQLException {
        final Injector injector = Guice.createInjector(new TaxiModule());

        TaxiDAO taxiDAO = injector.getInstance(TaxiDAO.class);
        play(taxiDAO);
    }

    private static void play(TaxiDAO taxiDAO) {

        System.out.println("users in db: " + taxiDAO.getAll());

        final Taxi taxi = Taxi.create("Gordon", "Mini Cooper", true);
        taxiDAO.insert(taxi);
        System.out.println("persisted " + taxi);
        System.out.println("users in db: " + taxiDAO.getAll());
        System.out.println();

        taxi.setDriverName("Morgan");
        taxiDAO.update(taxi);
        System.out.println("updated Gordon to Morgan");
        System.out.println("users in db: " + taxiDAO.getAll());
        System.out.println();


        taxiDAO.delete(taxi.getId());
        System.out.println("deleted user with id " + taxi.getId().getValue());
        System.out.println("users in db: " + taxiDAO.getAll());
        System.out.println();

        final Optional<Taxi> emptyMorgan = taxiDAO.get(taxi.getId());
        System.out.println("tried to get user by " + taxi.getId().getValue() + " but got " + emptyMorgan);
        System.out.println();
    }
}
