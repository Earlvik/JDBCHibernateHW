package ru.hhschool.earlvik.JDBCHibernateHW.Settings;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *  on 21.01.2015.
 */
public class Settings {
    public final DatabaseSettings database;

    Settings(final DatabaseSettings database) {
        this.database = checkNotNull(database);
    }
}
