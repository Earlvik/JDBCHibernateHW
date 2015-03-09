package ru.hhschool.earlvik.JDBCHibernateHW.Taxis;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 *  on 21.01.2015.
 */
public class TaxiSpringJDBCDAO implements TaxiDAO {

    protected final JdbcTemplate jdbcTemplate;
    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected final SimpleJdbcInsert simpleJdbcInsert;
    protected Logger logger = LoggerFactory.getLogger(TaxiDAO.class);



    @Inject
    public TaxiSpringJDBCDAO(@Named("Taxi") final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("taxis")
                .usingGeneratedKeyColumns("taxi_id");
    }

    @Override
    public void insert(Taxi taxi) {
        if (taxi.getId() != null) {
            logger.warn("Could not insert " + taxi + " with already assigned id");
            throw new IllegalArgumentException("can not insert " + taxi + " with already assigned id");
        }

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("driver_name", taxi.getDriverName());
        params.put("car", taxi.getCar());
        params.put("available", taxi.isAvailable());
        params.put("drives", taxi.getDrives());


        final int taxiId = simpleJdbcInsert.executeAndReturnKey(params).intValue();
        logger.info("Successfully inserted taxi: "+taxi.toString());
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
            logger.info("No taxi with id "+taxiId.getValue()+" was found");
            return Optional.empty();
        }
        logger.info("Retrieved taxi: "+taxi.toString());
        return Optional.of(taxi);
    }

    @Override
    public Set<Taxi> getAll() {
        final String query = "SELECT taxi_id, driver_name, car, available, drives FROM taxis";
        logger.info("retrieved all taxis");
        return ImmutableSet.copyOf(jdbcTemplate.query(query, rowToTaxi));
    }

    @Override
    public void update(Taxi taxi) {
        if (taxi.getId() == null) {
            logger.warn("Could not update " + taxi + " without id");
            throw new IllegalArgumentException("can not update " + taxi + " without id");
        }

        final String query = "UPDATE taxis SET" +
                " driver_name = :driver_name, car = :car," +
                " available = :available, drives = :drives WHERE taxi_id = :taxi_id";

        final ImmutableMap<String, Object> params = ImmutableMap.of(
                "taxi_id", taxi.getId().getValue(),
                "driver_name", taxi.getDriverName(),
                "car", taxi.getCar(),
                "available", taxi.isAvailable(),
                "drives", taxi.getDrives()
        );

        namedParameterJdbcTemplate.update(query, params);
        logger.info("Updated taxi with id "+taxi.getId().getValue());
    }

    @Override
    public void delete(TaxiId taxiId) {
        final String query = "DELETE FROM taxis WHERE taxi_id = :taxi_id";

        final ImmutableMap<String, Object> params = ImmutableMap.of("taxi_id", taxiId.getValue());

        namedParameterJdbcTemplate.update(query, params);
        logger.info("Deleted taxi with id "+taxiId.getValue());
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
