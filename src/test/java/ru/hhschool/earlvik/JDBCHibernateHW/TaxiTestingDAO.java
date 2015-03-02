package ru.hhschool.earlvik.JDBCHibernateHW;

import ru.hhschool.earlvik.JDBCHibernateHW.Taxis.TaxiSpringJDBCDAO;

import javax.sql.DataSource;

public class TaxiTestingDAO extends TaxiSpringJDBCDAO {


    public TaxiTestingDAO(DataSource dataSource) {
        super(dataSource);
        drop();
        create();
    }

    private void drop() {
        final String query = "DROP TABLE IF EXISTS taxis";
        jdbcTemplate.execute(query);
    }

    private void create() {
        final String query = "CREATE TABLE taxis (\n" +
                "  taxi_id int(10) unsigned NOT NULL AUTO_INCREMENT,\n" +
                "  driver_name varchar(20) DEFAULT NULL,\n" +
                "  car varchar(20) DEFAULT NULL,\n" +
                "  available bool DEFAULT false,\n" +
                "  drives int(10) unsigned NOT NULL DEFAULT 0,\n" +
                "  PRIMARY KEY (taxi_id)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8";

        jdbcTemplate.execute(query);
    }
}
