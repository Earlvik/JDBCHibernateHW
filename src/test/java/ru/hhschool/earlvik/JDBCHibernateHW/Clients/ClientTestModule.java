package ru.hhschool.earlvik.JDBCHibernateHW.Clients;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ClientTestModule extends AbstractModule{
    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    SessionFactory provideSessionFactory(){
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Client.class);
        configuration.setProperty("hibernate.dialect",
                "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class",
                "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");

        return configuration.buildSessionFactory();

    }


}
