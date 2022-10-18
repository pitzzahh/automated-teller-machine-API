package io.github.pitzzahh.atm.dao;

import static io.github.pitzzahh.util.utilities.classes.enums.Status.*;
import io.github.pitzzahh.atm.exceptions.ClientNotFoundException;
import io.github.pitzzahh.util.utilities.classes.enums.Status;
import io.github.pitzzahh.util.utilities.SecurityUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import io.github.pitzzahh.atm.mapper.ClientMapper;
import io.github.pitzzahh.atm.mapper.LoanMapper;
import io.github.pitzzahh.atm.entity.Client;
import io.github.pitzzahh.atm.entity.Loan;
import static java.lang.String.format;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.function.Consumer;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.*;

/**
 * Implementation of the {@link AtmDAO}.
 */
public class InDatabase implements AtmDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    /**
     * Function that accepts a {@code DataSource} object.
     * Object needed to connect to the database.
     * @return nothing
     * @throws RuntimeException if failed to connect to the database.
     * @see Consumer
     * @see DataSource
     */
    @Override
    public Consumer<DataSource> setDataSource() throws RuntimeException {
        return source -> {
            if (source == null) throw new UnsupportedOperationException("No DataSource provided, to use in memory database use InMemory class as DAO and do not provide a datasource.");
            try {
                this.dataSource = source;
                this.dataSource.getConnection().beginRequest();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        };
    }

    /**
     * Function that supplies a {@code Map<String, Client>}.
     * <p>{@code String} - the key, the key is the account number of the client</p>
     * <p>{@code Client} - the value, the value is the client object</p>
     * @return a {@code Client} object
     * @see Map
     * @see Supplier
     * @see Client
     */
    @Override
    public Supplier<Map<String, Client>> getAllClients() {
        return () -> jdbcTemplate.query("SELECT * FROM clients", new ClientMapper())
                .stream()
                .collect(Collectors.toMap(Client::accountNumber, Function.identity()));
    }

    /**
     * Function that returns a {@code Optional<Client>} object based on the {@code String}
     * that contains the account number.
     * <p>T - a {@code String} containing the account number to be search from the database.</p>
     * <p>R - a {@code Optional<Client>} containing the result if the client is found or not.</p>
     * @return a {@code Optional<Client>} object.
     * @throws IllegalArgumentException if the account number does not belong to any client.
     * @see Function
     * @see Client
     */
    @Override
    public Function<String, Client> getClientByAccountNumber() throws IllegalArgumentException {
        final var QUERY = "SELECT * FROM clients WHERE account_number = ?";
        return an -> {
            try {
                return jdbcTemplate.queryForObject(QUERY, new ClientMapper(), SecurityUtil.encrypt(an));
            } catch (RuntimeException ignored) {
                throw new ClientNotFoundException(format("CLIENT WITH ACCOUNT NUMBER: %s DOES NOT EXIST", an));
            }
        };
    }

    /**
     * Function that accepts a {@code String} containing the account number.
     * The account number will be used to search for the clients savings.
     * @return a {@code Double} containing the savings of the client with the account number.
     * @see Function
     */
    @Override
    public Function<String, Double> getClientSavingsByAccountNumber() {
        final var QUERY = "SELECT savings FROM clients WHERE account_number = ?";
        return accountNumber -> Double.parseDouble(
                SecurityUtil.decrypt(
                        Objects.requireNonNull(jdbcTemplate.queryForObject(
                                QUERY,
                                String.class,
                                SecurityUtil.encrypt(accountNumber)
                        ))
                )
        );
    }

    /**
     * Function that removes a client in the database using the account number.
     * <p>T - a {@code String} the account number of the client needed in order to remove the client.</p>
     * <p>R - the {@code Status} of the operation if {@link Status#SUCCESS} or {@link Status#ERROR}.</p>
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Function
     * @see Status
     */
    @Override
    public Function<String, Status> removeClientByAccountNumber() {
        return an -> jdbcTemplate.update("DELETE FROM clients WHERE account_number = ?", SecurityUtil.encrypt(an)) > 0 ? SUCCESS : ERROR;
    }

    /**
     * Function that removes all the clients in the database.
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Supplier
     * @see Status
     */
    @Override
    public Supplier<Status> removeAllClients() {
        return () -> jdbcTemplate.update("DELETE FROM clients WHERE TRUE") > 0 ? SUCCESS : ERROR;
    }

    /**
     * Function that accepts two values. A {@code String} and a {@code Boolean}.
     * <p>First parameter is a {@code String} contains the account number of the client.</p>
     * <p>Second parameter is a {@code Boolean}, {@code true} if the client account should be locked, default is false.</p>
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see BiFunction
     * @see Status
     */
    @Override
    public BiFunction<String, Boolean, Status> updateClientStatusByAccountNumber() {
        final var QUERY = "UPDATE clients SET isLocked = ? WHERE account_number = ?";
        return (an, status) -> jdbcTemplate.update(QUERY, status, SecurityUtil.encrypt(an)) > 0 ? SUCCESS : ERROR;
    }

    /**
     * Function that accepts two values. A {@code String} and a {@code Double}.
     * <p>First parameter is a {@code String} contains the account number of the client.</p>
     * <p>Second parameter is a {@code Double}, the new savings balance of the client.</p>
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see BiFunction
     * @see Status
     */
    @Override
    public BiFunction<String, Double, Status> updateClientSavingsByAccountNumber() {
        final var QUERY = "UPDATE clients SET savings = ? WHERE account_number = ?";
        return (an, newSavings) -> jdbcTemplate.update(
                QUERY,
                SecurityUtil.encrypt(String.valueOf(newSavings)),
                SecurityUtil.encrypt(an)
        ) > 0 ? SUCCESS : ERROR;
    }

    /**
     * Function that save a client to the database. The function takes a {@code Client} object,
     * the object to be saved in the database table.
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Function
     * @see Client
     * @see Status
     */
    @Override
    public Function<Client, Status> saveClient() {
        final var QUERY = "INSERT INTO clients (account_number, pin, first_name, last_name, gender, address, date_of_birth, savings, isLocked)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return client -> jdbcTemplate.update(
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
     * Function that saves a {@code Collection<Client} to the database table.
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Function
     * @see Collection
     * @see Client
     * @see Status
     */
    @Override
    public Function<Collection<Client>, Status> saveAllClients() {
        final var QUERY = "INSERT INTO clients (account_number, pin, first_name, last_name, gender, address, date_of_birth, savings, isLocked)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return clients -> {
            clients.forEach(
                        client -> jdbcTemplate.update(
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
                        )
                    );
            return SUCCESS;
        };
    }

    /**
     * Function that submits a loan request.
     * The Function takes a {@code Loan} object containing the loan information.
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Function
     * @see Loan
     * @see Status
     */
    @Override
    public Function<Loan, Status> requestLoan() {
        final var QUERY = "INSERT INTO loans(loan_number, account_number, date_of_loan, amount, pending, declined) VALUES(?, ?, ?, ?, ?, ?)";
        return loan ->  jdbcTemplate.update(
                QUERY,
                getLoanCount().apply(loan.accountNumber()),
                SecurityUtil.encrypt(loan.accountNumber()),
                loan.dateOfLoan(),
                SecurityUtil.encrypt(String.valueOf(loan.amount())),
                SecurityUtil.encrypt(String.valueOf(loan.pending())),
                SecurityUtil.encrypt(String.valueOf(loan.isDeclined()))
        ) > 0 ? SUCCESS : ERROR;
    }

    /**
     * Function that returns a key value pair, a {@code Map<String, List<Loan>>} in particular.
     * <p>K - is a {@code String} containing the key, the key is the account number of the client who requested a loan.</p>
     * <p>V - is a {@code String} containing the value, the value is all the loans that the account requested. It is a {@code List<Loan>}</p>
     * @return a {@code Map<String, List<Loan>>} a key value pair containing all the loans from the table in the database.
     * @see Supplier
     * @see Map
     * @see List
     * @see Loan
     */
    @Override
    public Supplier<Map<String, List<Loan>>> getAllLoans() {
        return () -> jdbcTemplate.query("SELECT * FROM loans", new LoanMapper())
                .stream()
                .collect(Collectors.groupingBy(Loan::accountNumber));
    }

    /**
     * Function that gets the loan of a client using loan number and account number.
     * The function takes an {@code Integer} and a {@code String}, the integer containing the loan number,
     * and the string containing the account number.
     * @return an {@code Optional<Loan>} whether the loan exist or not.
     * @see BiFunction
     * @see Optional
     * @see Loan
     */
    @Override
    public BiFunction<Integer, String, Optional<Loan>> getLoanByLoanNumberAndAccountNumber() {
        final var QUERY = "SELECT * FROM loans WHERE loan_number = ? AND account_number = ?";
        return (loanNumber, accountNumber) -> Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        QUERY,
                        new LoanMapper(),
                        loanNumber,
                        SecurityUtil.encrypt(accountNumber))
        );
    }

    /**
     * Function that gets the latest loan count of a client and returns the count.
     * The function takes a {@code String} containing the account number that holds a loan.
     * @return a {@code Integer} containing the loan count of a client.
     * @see Function
     */
    @Override
    public Function<String, Integer> getLoanCount() {
        final var QUERY = "SELECT MAX(loan_number) FROM loans WHERE account_number = ?";
        return accountNumber ->  {
            int result = Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            QUERY,
                            Integer.class,
                            SecurityUtil.encrypt(accountNumber))
            ).orElse(0);
            return result >= 1 ? result + 1 : 1;
        };
    }

    /**
     * Function that approves a loan request.
     * The function takes a {@code Loan} object containing the loan information to be approved.
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @throws IllegalArgumentException if the account number does not belong to any client, thus the message cannot be found.
     * @see BiFunction
     * @see Loan
     * @see Status
     */
    @Override
    public BiFunction<Loan, Client, Status> approveLoan() throws IllegalArgumentException {
        final var QUERY = "UPDATE loans SET pending = ? WHERE loan_number = ? AND account_number = ?";
        return (loan, c) -> {
            var client = getClientByAccountNumber().apply(c.accountNumber());
            var status = updateClientSavingsByAccountNumber().apply(client.accountNumber(), client.savings() + loan.amount());
            return status == SUCCESS ? jdbcTemplate.update(
                    QUERY,
                    SecurityUtil.encrypt(Boolean.FALSE.toString()),
                    loan.loanNumber(),
                    SecurityUtil.encrypt(loan.accountNumber())
            ) > 0 ? SUCCESS : ERROR : CANNOT_PERFORM_OPERATION;
        };
    }

    /**
     * Function that declines a loan request.
     * The function takes a {@code Loan} object containing the loan information to be approved.
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Function
     * @see Loan
     * @see Status
     */
    @Override
    public Function<Loan, Status> declineLoan() {
        final var QUERY = "UPDATE loans SET declined = ? WHERE loan_number = ? AND account_number = ?";
        return loan -> jdbcTemplate.update(
                QUERY,
                SecurityUtil.encrypt(Boolean.TRUE.toString()),
                loan.loanNumber(),
                SecurityUtil.encrypt(loan.accountNumber())
        ) > 0 ? SUCCESS : ERROR;
    }

    /**
     * Function that removes a loan.
     * The function takes a {@code Loan} object containing the loan information to be removed.
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Function
     * @see Loan
     * @see Status
     */
    @Override
    public Function<Loan, Status> removeLoan() {
        final var QUERY = "DELETE FROM loans WHERE loan_number = ? AND account_number = ? AND pending = ?";
        return loan -> jdbcTemplate.update(
                QUERY,
                loan.loanNumber(),
                loan.accountNumber(),
                SecurityUtil.encrypt(Boolean.TRUE.toString())
        ) > 0 ? SUCCESS : ERROR;
    }

    /**
     * Function that removes all the loans from the database.
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Supplier
     * @see Status
     */
    @Override
    public Supplier<Status> removeAllLoans() {
        return () ->  jdbcTemplate.update("DELETE FROM loans WHERE TRUE") > 0 ? SUCCESS : ERROR;
    }

}
