package io.github.pitzzahh.database;

import io.github.pitzzahh.service.ClientService;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Interface used by services to gain access to the database.
 */
public interface DatabaseConnection {

    /**
     * Setup the datasource.
     * @see ClientService
     * @return the datasource.
     */
    static DriverManagerDataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost/atm");
        dataSource.setUsername("postgres");
        dataSource.setPassword("!Password123");
        return dataSource;
    }

}
