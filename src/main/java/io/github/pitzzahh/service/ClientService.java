package io.github.pitzzahh.service;

import io.github.pitzzahh.dao.ClientDAOImplementation;
import io.github.pitzzahh.database.DatabaseConnection;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * The service for managing products.
 */
public class ClientService extends ClientDAOImplementation implements DatabaseConnection {

    /**
     * Gets the datasource from the {@link DatabaseConnection} interface.
     * @return the datasource to be used.
     */
    public static DriverManagerDataSource getDataSource() {
        return DatabaseConnection.getDataSource();
    }

}
