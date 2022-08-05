package io.github.pitzzahh.dao;

import java.util.*;
import javax.sql.DataSource;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.BiFunction;
import io.github.pitzzahh.entity.Loan;
import io.github.pitzzahh.entity.Client;
import io.github.pitzzahh.entity.Message;
import io.github.pitzzahh.mapper.LoanMapper;
import io.github.pitzzahh.mapper.ClientMapper;
import com.github.pitzzahh.utilities.SecurityUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import com.github.pitzzahh.utilities.classes.enums.Status;
import static com.github.pitzzahh.utilities.classes.enums.Status.ERROR;
import static com.github.pitzzahh.utilities.classes.enums.Status.SUCCESS;

/**
 * Implementation of the {@link AtmDAO}.
 */
public class AtmDAOImplementation implements AtmDAO {

    private DataSource dataSource;
    private JdbcTemplate db;

    @Override
    public Consumer<DataSource> setDataSource() {
        return source -> {
            this.dataSource = source;
            this.db = new JdbcTemplate(dataSource);
        };
    }

    @Override
    public Supplier<Map<String, Client>> getAllClients() {
        return () -> db.query("SELECT * FROM clients", new ClientMapper())
                .stream()
                .collect(Collectors.toMap(Client::accountNumber, Function.identity()));
    }

    @Override
    public Function<String, Optional<Client>> getClientByAccountNumber() {
        final var QUERY = "SELECT * FROM clients WHERE account_number = ?";
        return an -> Optional.ofNullable(Objects.requireNonNull(db.queryForObject(QUERY, new ClientMapper(), SecurityUtil.encrypt(an))));
    }

    /**
     * Function that accepts a {@code String} containing the account number.
     * The account number will be used to search for the clients savings.
     * @return a {@code Double} containing the savings of the client with the account number.
     */
    @Override
    public Function<String, Double> getClientSavingsByAccountNumber() {
        final var QUERY = "SELECT savings FROM clients WHERE account_number = ?";
        return accountNumber -> Double.parseDouble(
                SecurityUtil.decrypt(
                        db.queryForObject(
                                QUERY,
                                String.class,
                                SecurityUtil.encrypt(accountNumber)
                        )
                )
        );
    }

    @Override
    public Function<String, Status> removeClientByAccountNumber() {
        return an -> db.update("DELETE FROM clients WHERE account_number = ?", SecurityUtil.encrypt(an)) > 0 ? SUCCESS : ERROR;
    }

    /**
     * @return
     */
    @Override
    public Supplier<Status> removeAllClients() {
        return () -> db.update("DELETE FROM clients") > 0 ? SUCCESS : ERROR;
    }

    /**
     * Updates the clients pin attempts.
     * @return a {@code Status}
     */
    @Override
    public BiFunction<String, Boolean, Status> updateClientAttemptsByAccountNumber() {
        final var QUERY = "UPDATE clients SET isLocked = ? WHERE account_number = ?";
        return (an, status) -> db.update(QUERY, status, SecurityUtil.encrypt(an)) > 0 ? SUCCESS : ERROR;
    }

    /**
     * Updates the clients savings.
     * @return a {@code Status}
     */
    @Override
    public BiFunction<String, Double, Status> updateClientSavingsByAccountNumber() {
        final var QUERY = "UPDATE clients SET savings = ? WHERE account_number = ?";
        return (an, newSavings) -> db.update(
                QUERY,
                SecurityUtil.encrypt(String.valueOf(newSavings)),
                SecurityUtil.encrypt(an)
        ) > 0 ? SUCCESS : ERROR;
    }

    /**
     * Saves a client.
     * @return
     */
    @Override
    public Function<Client, Status> saveClient() {
        final var QUERY = "INSERT INTO clients (account_number, pin, first_name, last_name, gender, address, date_of_birth, savings, isLocked)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return client -> db.update(
                QUERY,
                SecurityUtil.encrypt(client.accountNumber()),
                SecurityUtil.encrypt(client.pin()),
                SecurityUtil.encrypt(client.details().getFirstName()),
                SecurityUtil.encrypt(client.details().getLastName()),
                client.details().getGender().toString(),
                SecurityUtil.encrypt(client.details().getAddress()),
                client.details().getBirthDate(),
                SecurityUtil.encrypt(String.valueOf(client.savings())),
                client.isLocked()
        ) > 0 ? SUCCESS : ERROR;
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
        final var QUERY = "INSERT INTO loans(loan_number, account_number, date_of_loan, amount, pending) VALUES(?, ?, ?, ?, ?)";
        return loan ->  db.update(
                QUERY,
                getLoanCount().apply(loan.accountNumber()),
                SecurityUtil.encrypt(loan.accountNumber()),
                loan.dateOfLoan(),
                SecurityUtil.encrypt(String.valueOf(loan.amount())),
                SecurityUtil.encrypt(String.valueOf(loan.pending()))
        ) > 0 ? SUCCESS : ERROR;
    }

    /**
     * @return
     */
    @Override
    public Supplier<Map<String, List<Loan>>> getAllLoans() {
        return () -> db.query("SELECT * FROM loans", new LoanMapper())
                .stream()
                .collect(Collectors.groupingBy(Loan::accountNumber));
    }

    /**
     * @return
     */
    @Override
    public BiFunction<Integer, String, Optional<Loan>> getLoanByLoanNumberAndAccountNumber() {
        final var QUERY = "SELECT * FROM loans WHERE loan_number = ? AND account_number = ?";
        return (loanNumber, accountNumber) -> Optional.ofNullable(
                Objects.requireNonNull(
                        db.queryForObject(
                                QUERY,
                                new LoanMapper(),
                                loanNumber,
                                SecurityUtil.encrypt(accountNumber))
                )
        );
    }

    /**
     * @return
     */
    @Override
    public Function<String, Integer> getLoanCount() {
        final var QUERY = "SELECT MAX(loan_number) FROM loans WHERE account_number = ?";
        return accountNumber ->  {
            var result = Optional.ofNullable(
                    db.queryForObject(
                            QUERY,
                            Integer.class,
                            SecurityUtil.encrypt(accountNumber))
            ).orElse(0);
            return result >= 1 ? result + 1 : 1;
        };
    }

    /**
     * @return
     */
    @Override
    public Function<Loan, Status> approveLoan() {
        final var QUERY = "UPDATE loans SET pending = ? WHERE loan_number = ? AND account_number = ?";
        return loan -> db.update(
                QUERY,
                SecurityUtil.encrypt(Boolean.FALSE.toString()),
                loan.loanNumber(),
                SecurityUtil.encrypt(loan.accountNumber())
        ) > 0 ? SUCCESS : ERROR;
    }

    /**
     * @return
     */
    @Override
    public Function<Loan, Status> removeLoan() {
        final var QUERY = "DELETE FROM loans WHERE loan_number = ? AND account_number = ? AND pending = ?";
        return loan -> db.update(
                QUERY,
                loan.loanNumber(),
                loan.accountNumber(),
                SecurityUtil.encrypt(Boolean.TRUE.toString())
        ) > 0 ? SUCCESS : ERROR;
    }

    /**
     * @return
     */
    @Override
    public Supplier<Status> removeAllLoans() {
        return () ->  db.update("DELETE FROM loans") > 0 ? SUCCESS : ERROR;
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
