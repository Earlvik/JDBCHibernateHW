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
    SessionFactory provideSessionFactory(){
        return HibernateConfig.prod().buildSessionFactory();
    }
}
