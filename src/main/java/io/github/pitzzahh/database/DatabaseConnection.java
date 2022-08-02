package io.github.pitzzahh.database;

import java.util.stream.Stream;
import io.github.pitzzahh.service.AtmService;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Interface used by services to gain access to the database.
 */
public class DatabaseConnection {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public DatabaseConnection setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        return this;
    }

    public DatabaseConnection setUrl(String url) {
        this.url = url;
        return this;
    }

    public DatabaseConnection setUsername(String username) {
        this.username = username;
        return this;
    }

    public DatabaseConnection setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Returns the complete data source.
     * @return the datasource.
     * @see AtmService
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
