package io.github.pitzzahh.dao;

import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.BiFunction;
import io.github.pitzzahh.entity.Client;
import static io.github.pitzzahh.dao.Queries.*;
import org.springframework.jdbc.core.JdbcTemplate;
import com.github.pitzzahh.utilities.classes.enums.Status;

/**
 * Implementation of the {@link ClientDAO}.
 */
public class ClientDAOImplementation implements ClientDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Override
    public Consumer<DataSource> setDataSource() {
        return source -> {
            this.dataSource = source;
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        };
    }

    @Override
    public Supplier<Map<String, Client>> getAllClients() {
        return getAllClientsQuery(jdbcTemplate);
    }

    @Override
    public Function<String, Optional<Client>> getClientByAccountNumber() {
        return an -> getClientByAccountNumberQuery(an, jdbcTemplate);
    }

    @Override
    public Function<String, Status> removeClientByAccountNumber() {
        return an -> removeClientByAccountNumberQuery(an, jdbcTemplate);
    }

    /**
     * Updates the clients pin attempts.
     * @return a {@code Status}
     */
    @Override
    public BiFunction<String, String, Status> updateClientAttemptsByAccountNumber() {
        return (an, attempts) -> updateClientAttemptsByAccountNumberQuery(an, attempts, jdbcTemplate);
    }

    /**
     * Updates the clients savings.
     * @return a {@code Status}
     */
    @Override
    public BiFunction<String, Double, Status> updateClientSavingsByAccountNumber() {
        return (an, newSavings) -> updateClientSavingsByAccountNumberQuery(an, newSavings, jdbcTemplate);
    }

    @Override
    public Function<Client, Status> saveClient() {
        return client -> saveClientQuery(client, jdbcTemplate);
    }

}
