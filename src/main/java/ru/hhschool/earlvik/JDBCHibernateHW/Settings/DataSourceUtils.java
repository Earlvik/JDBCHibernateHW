package ru.hhschool.earlvik.JDBCHibernateHW.Settings;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.SQLException;

/**
 *  on 21.01.2015.
 */
public class DataSourceUtils {
    private DataSourceUtils() {
    }

    public static MysqlDataSource mysqlDataSource(final String dbUrl, final String dbUser, final String dbPassword)
            throws SQLException {

        final MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl(dbUrl);
        mysqlDataSource.setUser(dbUser);
        mysqlDataSource.setPassword(dbPassword);
        return mysqlDataSource;
    }
}
