package io.github.pitzzahh.dao;

import java.util.Map;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.BiFunction;
import io.github.pitzzahh.entity.Loan;
import io.github.pitzzahh.entity.Client;
import io.github.pitzzahh.entity.Message;
import org.springframework.jdbc.core.JdbcTemplate;
import static io.github.pitzzahh.database.Queries.*;
import com.github.pitzzahh.utilities.classes.enums.Status;

/**
 * Implementation of the {@link AtmDAO}.
 */
public class AtmDAOImplementation implements AtmDAO {

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
     * @return
     */
    @Override
    public Supplier<Status> removeAllClients() {
        return () -> removeAllClientsQuery(jdbcTemplate);
    }

    /**
     * Updates the clients pin attempts.
     * @return a {@code Status}
     */
    @Override
    public BiFunction<String, Boolean, Status> updateClientAttemptsByAccountNumber() {
        return (an, status) -> updateClientStatusByAccountNumber(an, status, jdbcTemplate);
    }

    /**
     * Updates the clients savings.
     * @return a {@code Status}
     */
    @Override
    public BiFunction<String, Double, Status> updateClientSavingsByAccountNumber() {
        return (an, newSavings) -> updateClientSavingsByAccountNumberQuery(an, newSavings, jdbcTemplate);
    }

    /**
     * Saves a client.
     * @return
     */
    @Override
    public Function<Client, Status> saveClient() {
        return client -> saveClientQuery(client, jdbcTemplate);
    }

    /**
     * @return
     */
    @Override
    // TODO: implement
    public Function<Collection<Client>, Status> saveAllClient() {
        return null;
    }

    /**
     * Makes a loan
     * @return a {@code Status}
     */
    @Override
    public Function<Loan, Status> requestLoan() {
        return loan -> requestLoanQuery(loan, jdbcTemplate);
    }

    /**
     * @return
     */
    @Override
    public  Supplier<Map<String, List<Loan>>> getAllLoans() {
        return getAllLoansQuery(jdbcTemplate);
    }

    /**
     * @return
     */
    @Override
    public BiFunction<Integer, String, Optional<Loan>> getLoanByLoanNumberAndAccountNumber() {
        return (loanNumber, accountNumber) -> getLoanByLoanNumberAndAccountNumberQuery(loanNumber, accountNumber, jdbcTemplate);
    }

    /**
     * @return
     */
    @Override
    public Function<String, Integer> getLoanCount() {
        return accountNumber -> getLoanCountQuery(accountNumber, jdbcTemplate);
    }

    /**
     * @return
     */
    @Override
    public Function<Loan, Status> approveLoan() {
        return loan -> approveLoanQuery(loan, jdbcTemplate);
    }

    /**
     * @return
     */
    @Override
    public Function<Loan, Status> removeLoan() {
        return loan -> removeLoanQuery(loan, jdbcTemplate);
    }

    /**
     * @return
     */
    @Override
    public Supplier<Status> removeAllLoans() {
        return () -> removeAllLoansQuery(jdbcTemplate);
    }

    /**
     * @return
     */
    @Override
    // TODO: implement
    public Function<Message, Status> addMessage() {
        return null;
    }

    /**
     * @return
     */
    @Override
    // TODO: implement
    public BiFunction<Integer, String, Message> getMessage() {
        return null;
    }

}
