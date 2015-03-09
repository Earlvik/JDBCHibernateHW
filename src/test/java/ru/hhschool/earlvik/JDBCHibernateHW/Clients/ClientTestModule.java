package ru.hhschool.earlvik.JDBCHibernateHW.Clients;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.hhschool.earlvik.JDBCHibernateHW.HibernateConfig;

public class ClientTestModule extends AbstractModule{
    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    SessionFactory provideSessionFactory(){
        return HibernateConfig.prod()
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:test;INIT=CREATE SCHEMA IF NOT EXISTS homework")
                .setProperty("hibernate.connection.username","sa")
                .setProperty("hibernate.connection.password", "")
                .setProperty("hibernate.show_sql","false")
                .setProperty("hibernate.hbm2ddl.auto","create-drop")
                .buildSessionFactory();

    }


}
