package ru.hhschool.earlvik.JDBCHibernateHW.Clients;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.hibernate.SessionFactory;
import ru.hhschool.earlvik.JDBCHibernateHW.HibernateConfig;
import ru.hhschool.earlvik.JDBCHibernateHW.Settings.Settings;
import ru.hhschool.earlvik.JDBCHibernateHW.Settings.SettingsUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

public class ClientModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ClientDAO.class).to(ClientHibernateDAO.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    Settings provideSettings() {
        return SettingsUtils.loadSettings();
    }

    @Provides
    @Singleton
    DataSource provideDataSource(final Settings settings) throws SQLException {
        final MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(settings.database.url);
        dataSource.setUser(settings.database.user);
        dataSource.setPassword(settings.database.password);
        return dataSource;
    }

    @Provides
    @Singleton
    SessionFactory provideSessionFactory(){
        return HibernateConfig.prod().buildSessionFactory();
    }
}
