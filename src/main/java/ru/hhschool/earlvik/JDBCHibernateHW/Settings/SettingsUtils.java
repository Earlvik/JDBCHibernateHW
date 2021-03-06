package ru.hhschool.earlvik.JDBCHibernateHW.Settings;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 *  on 21.01.2015.
 */
public class SettingsUtils {
    public static Settings loadSettings() {

        final Config config = ConfigFactory.load();
        return settingsFromConfig(config);
    }

    static Settings settingsFromConfig(final Config config) {

        final Config databaseConfig = config.getConfig("database");
        final DatabaseSettings databaseSettings = databaseSettingsFromDatabaseConfig(databaseConfig);
        return new Settings(databaseSettings);
    }

    private static DatabaseSettings databaseSettingsFromDatabaseConfig(final Config databaseConfig) {

        return new DatabaseSettings(
                databaseConfig.getString("url"),
                databaseConfig.getString("user"),
                databaseConfig.getString("password")
        );
    }

    private SettingsUtils() {
    }
}
