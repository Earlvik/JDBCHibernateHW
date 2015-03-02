package ru.hhschool.earlvik.JDBCHibernateHW;

import org.hibernate.cfg.Configuration;
import ru.hhschool.earlvik.JDBCHibernateHW.Clients.Client;

/**
 *  on 21.01.2015.
 */
public class HibernateConfig {

    public static Configuration prod() {
        return new Configuration().addAnnotatedClass(Client.class);
    }

    private HibernateConfig() {

    }
}
