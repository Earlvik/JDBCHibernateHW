package ru.hhschool.earlvik.JDBCHibernateHW.Taxis;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Earlviktor on 21.01.2015.
 */
public class TaxiSpringJDBCDAO implements TaxiDAO {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public TaxiSpringJDBCDAO(final DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("taxis")
                .usingGeneratedKeyColumns("taxi_id");
    }

    @Override
    public void insert(Taxi taxi) {
        if (taxi.getId() != null) {
            throw new IllegalArgumentException("can not insert " + taxi + " with already assigned id");
        }

        final Map<String, Object> params = new HashMap<String,Object>();
        params.put("driver_name", taxi.getDriverName());
        params.put("car", taxi.getCar());
        params.put("available", taxi.isAvailable());
        params.put("drives", taxi.getDrives());


        final int taxiId = simpleJdbcInsert.executeAndReturnKey(params).intValue();

        taxi.setId(new TaxiId(taxiId));
    }

    @Override
    public Optional<Taxi> get(TaxiId taxiId) {
        final String query = "SELECT taxi_id, driver_name, car, available, drives FROM taxis WHERE taxi_id = :taxi_id";

        final ImmutableMap<String, Object> params = ImmutableMap.of("taxi_id", taxiId.getValue());

        final Taxi taxi;
        try {
            taxi = namedParameterJdbcTemplate.queryForObject(query, params, rowToTaxi);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.of(taxi);
    }

    @Override
    public Set<Taxi> getAll() {
        final String query = "SELECT taxi_id, driver_name, car, available, drives FROM taxis";

        return ImmutableSet.copyOf(jdbcTemplate.query(query, rowToTaxi));
    }

    @Override
    public void update(Taxi taxi) {
        if (taxi.getId() == null) {
            throw new IllegalArgumentException("can not update " + taxi + " without id");
        }

        final String query = "UPDATE taxis SET" +
                " driver_name = :driver_name, car = :car," +
                " available = :available, drives = :drives WHERE taxi_id = :taxi_id";

        final ImmutableMap<String, Object> params = ImmutableMap.of(
                "taxi_id",taxi.getId().getValue(),
                "driver_name",taxi.getDriverName(),
                "car", taxi.getCar(),
                "available", taxi.isAvailable(),
                "drives", taxi.getDrives()
        );

        namedParameterJdbcTemplate.update(query, params);
    }

    @Override
    public void delete(TaxiId taxiId) {
        final String query = "DELETE FROM taxis WHERE taxi_id = :taxi_id";

        final ImmutableMap<String, Object> params = ImmutableMap.of("taxi_id", taxiId.getValue());

        namedParameterJdbcTemplate.update(query, params);
    }

    @Override
    public void drop() {
        final String query = "DROP TABLE IF EXISTS taxis";
        jdbcTemplate.execute(query);
    }

    @Override
    public void create(){
        final String query ="CREATE TABLE taxis (\n" +
                "  taxi_id int(10) unsigned NOT NULL AUTO_INCREMENT,\n" +
                "  driver_name varchar(20) DEFAULT NULL,\n" +
                "  car varchar(20) DEFAULT NULL,\n" +
                "  available bool DEFAULT false,\n" +
                "  drives int(10) unsigned NOT NULL DEFAULT 0,\n"+
                "  PRIMARY KEY (taxi_id)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8";

        jdbcTemplate.execute(query);
    }

    private static final RowMapper<Taxi> rowToTaxi = (resultSet, rowNum) ->
            Taxi.existing(
                    new TaxiId(resultSet.getInt("taxi_id")),
                    resultSet.getString("driver_name"),
                    resultSet.getString("car"),
                    resultSet.getBoolean("available"),
                    resultSet.getInt("drives")
            );
}
