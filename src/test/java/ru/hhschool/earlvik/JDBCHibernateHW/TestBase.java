package ru.hhschool.earlvik.JDBCHibernateHW;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import ru.hhschool.earlvik.JDBCHibernateHW.Clients.ClientModule;
import ru.hhschool.earlvik.JDBCHibernateHW.Clients.ClientTestModule;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiModule;
import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiTestModule;

public class TestBase {

    private static final Injector injector = Guice.createInjector(
            Modules.override(new ClientModule()).with(new ClientTestModule()), Modules.override(new TaxiModule()).with(new TaxiTestModule())

    );

    protected static <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }



}
