package ru.hhschool.earlvik.JDBCHibernateHW;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.hhschool.earlvik.JDBCHibernateHW.Settings.Settings;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.*;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import static ru.hhschool.earlvik.JDBCHibernateHW.Settings.DataSourceUtils.mysqlDataSource;
import static ru.hhschool.earlvik.JDBCHibernateHW.Settings.SettingsUtils.loadSettings;

/**
 *
 */

public class TaxiTest extends TestBase{

    private TaxiService taxiService;



    @Before
    public void prepare() {
        TaxiDAO taxiDAO = getInstance(TaxiDAO.class);
        taxiService = new TaxiService(taxiDAO);
    }

    @Test
    public void insertNonExistingTaxiGetHimAndCheckParametersTest() {
        Taxi taxi = Taxi.create("Bob", "Mercedes", true);

        taxiService.insert(taxi);

        Optional<Taxi> returned = taxiService.get(taxi.getId());
        Assert.assertTrue("Inserted user was not found in database: " + taxi, returned.isPresent());
        Taxi returnedTaxi = returned.get();
        Assert.assertTrue("Returned taxi: " + returnedTaxi + " was not equal to " + taxi,
                taxi.equals(returnedTaxi));
    }

    @Test
    public void insertTaxiWithNullName() {
        Taxi taxi = Taxi.create(null, "Mercedes", true);

        taxiService.insert(taxi);

        Optional<Taxi> returned = taxiService.get(taxi.getId());
        Assert.assertTrue("Inserted user was not found in database: " + taxi, returned.isPresent());
        Taxi returnedTaxi = returned.get();
        Assert.assertTrue("Returned taxi: " + returnedTaxi + " was not equal to " + taxi,
                taxi.equals(returnedTaxi));
    }

    @Test
    public void insertTaxiWithNullCar() {
        Taxi taxi = Taxi.create("Bob", null, true);

        taxiService.insert(taxi);

        Optional<Taxi> returned = taxiService.get(taxi.getId());
        Assert.assertTrue("Inserted user was not found in database: " + taxi, returned.isPresent());
        Taxi returnedTaxi = returned.get();
        Assert.assertTrue("Returned taxi: " + returnedTaxi + " was not equal to " + taxi,
                taxi.equals(returnedTaxi));
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertUserWithIdGetIllegalArgumentExceptionTest() {

        Taxi taxi = Taxi.create("Bob", "Mercedes", true);
        taxi.setId(new TaxiId(12));

        taxiService.insert(taxi);

    }

    @Test
    public void insertSeveralTaxisGetAllTest() {
        Taxi bob = Taxi.create("Bob", "Mercedes", true);
        Taxi steve = Taxi.create("Steve", "Honda", false);

        taxiService.insert(bob);
        taxiService.insert(steve);

        Set<Taxi> taxis = taxiService.getAll();
        Assert.assertNotNull("The returned set of results was null", taxis);
        Assert.assertTrue("The size of set was not 2, but " + taxis.size(), taxis.size() == 2);
        Assert.assertTrue("No Bob returned, only " + taxis, taxis.stream().anyMatch(x -> x.equals(bob)));
        Assert.assertTrue("No Steve returned, only " + taxis, taxis.stream().anyMatch(x -> x.equals(steve)));
    }

    @Test
    public void insertAndUpdateTaxiGetUpdatedVersionTest() {
        Taxi taxi = Taxi.create("Bob", "Mercedes", true);

        taxiService.insert(taxi);
        TaxiId id = taxi.getId();
        taxi.setCar("BMW");
        taxi.setDriverName("Peter");
        taxi.setAvailable(false);
        taxiService.update(taxi);

        Optional<Taxi> returned = taxiService.get(id);
        Assert.assertTrue(returned.isPresent());
        Assert.assertTrue(returned.get().equals(taxi));
    }


    @Test
    public void insertAndDeleteTaxiGetNothingTest() {
        Taxi taxi = Taxi.create("Bob", "Mercedes", true);

        taxiService.insert(taxi);
        TaxiId id = taxi.getId();
        taxiService.delete(id);

        Optional<Taxi> returned = taxiService.get(id);
        Assert.assertFalse("Bob is still found, though deleted", returned.isPresent());
        Set<Taxi> taxis = taxiService.getAll();
        Assert.assertTrue("Bob is still found in all taxis", taxis.stream().noneMatch(x -> x.equals(taxi)));
    }


}
