package io.github.pitzzahh.atm.database;

import io.github.pitzzahh.atm.service.AtmService;
import io.github.pitzzahh.atm.dao.AtmDAOImplementation;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Interface used by services to gain access to the database.
 */
public class DatabaseConnection {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    /**
     * Sets the driver class name.
     * <p>Examples:</p>
     * <p>postgres: org.postgresql.Driver</p>
     * <p>mysql: com.mysql.cj.jdbc.Driver</p>
     * @param driverClassName the driver class name
     * @return a {@code DatabaseConnection} object.
     * @see org.postgresql.Driver
     */
    public DatabaseConnection setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        return this;
    }

    /**
     * Sets the url of the database.
     * <p>Examples:</p>
     * <p>postgres: jdbc:postgresql://localhost/{@code <name of the database>}</p>
     * <p>mysql: jdbc:mysql://host1:33060/{@code <name of the database>}</p>
     * @param url the url to the database.
     * @return a {@code DatabaseConnection} object.
     * @see org.postgresql.Driver
     */
    public DatabaseConnection setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Sets the username of the database.
     * @param username the username.
     * @return a {@code DatabaseConnection} object.
     */
    public DatabaseConnection setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Sets the password of the database.
     * @param password the password.
     * @return a {@code DatabaseConnection} object.
     */
    public DatabaseConnection setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Returns the complete data source.
     * @return the datasource.
     * @see AtmService
     * @see AtmDAOImplementation
     */
    public DriverManagerDataSource getDataSource() {
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(this.driverClassName);
        dataSource.setUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);
        return dataSource;
    }

}
