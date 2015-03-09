package ru.hhschool.earlvik.JDBCHibernateHW.Taxis;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.hhschool.earlvik.JDBCHibernateHW.TaxiTestingDAO;

import javax.sql.DataSource;

public class TaxiTestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TaxiDAO.class).to(TaxiTestingDAO.class);
    }

    @Provides
    @Singleton
    DataSource provideDataSource() {

        final EmbeddedDatabase embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        closeEmbeddedDatabaseOnShutdown(embeddedDatabase);
        return embeddedDatabase;
    }

    private static void closeEmbeddedDatabaseOnShutdown(final EmbeddedDatabase embeddedDatabase) {
        Runtime.getRuntime().addShutdownHook(new Thread(embeddedDatabase::shutdown));
    }
}
