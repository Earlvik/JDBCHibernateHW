package ru.hhschool.earlvik.JDBCHibernateHW.Taxis;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import ru.hhschool.earlvik.JDBCHibernateHW.Settings.Settings;
import ru.hhschool.earlvik.JDBCHibernateHW.Settings.SettingsUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

public class TaxiModule extends AbstractModule {


    @Override
    protected void configure() {
        bind(TaxiDAO.class).to(TaxiSpringJDBCDAO.class).in(Singleton.class);
    }


    @Provides
    @Singleton
    @Named("Taxi")
    Settings provideSettings() {
        return SettingsUtils.loadSettings();
    }

    @Provides
    @Singleton
    @Named("Taxi")
    DataSource provideDataSource(final Settings settings) throws SQLException {
        final MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(settings.database.url);
        dataSource.setUser(settings.database.user);
        dataSource.setPassword(settings.database.password);
        return dataSource;
    }
}
