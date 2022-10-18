package io.github.pitzzahh.atm.dao;

import static io.github.pitzzahh.util.utilities.classes.enums.Status.ERROR;
import static io.github.pitzzahh.util.utilities.classes.enums.Status.SUCCESS;
import io.github.pitzzahh.atm.exceptions.ClientNotFoundException;
import io.github.pitzzahh.util.utilities.classes.enums.Status;
import io.github.pitzzahh.atm.entity.Client;
import io.github.pitzzahh.atm.entity.Loan;
import static java.lang.String.format;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.*;

/**
 * Saves the data in memory.
 * Class used to access the database. Implemented by {@code InDatabase}.
 */
public class InMemory implements AtmDAO {

    /**
     * Stores the {@code Client} objects.
     * The {@code Map<String, Client>} object.
     */
    private final Map<String, Client> CLIENTS = new HashMap<>();

    /**
     * Stores the {@code Loan} objects.
     * The {@code Map<String, Loan>} object.
     */
    private final Map<String, Loan> LOANS = new HashMap<>();

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
        return () -> CLIENTS;
    }

    /**
     * Function that returns a {@code Optional<Client>} object based on the {@code String}
     * that contains the account number.
     * <p>T - a {@code String} containing the account number to be search from the database.</p>
     * <p>R - a {@code Optional<Client>} containing the result if the client is found or not.</p>
     * @return a {@code Optional<Client>} object.
     * @throws IllegalArgumentException if the account number does not belong to any client.
     * @see Optional
     * @see Function
     * @see Client
     */
    @Override
    public Function<String, Optional<Client>> getClientByAccountNumber() throws IllegalArgumentException {
        return accountNumber -> Optional.ofNullable(CLIENTS.entrySet()
                .stream()
                .filter(e -> e.getKey().equals(accountNumber))
                .findAny()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new ClientNotFoundException(format("Client with account number [%s] does not exist", accountNumber))));
    }

    /**
     * Function that accepts a {@code String} containing the account number.
     * The account number will be used to search for the clients savings.
     * @return a {@code Double} containing the savings of the client with the account number.
     * @see Function
     */
    @Override
    public Function<String, Double> getClientSavingsByAccountNumber() {
        return accountNumber -> getClientByAccountNumber().apply(accountNumber).map(Client::savings).orElse(0.0);
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
        return accountNumber -> {
            var isPresent = CLIENTS.containsKey(accountNumber);
            if (isPresent) CLIENTS.remove(accountNumber);
            else throw new ClientNotFoundException(format("Client with account number [%s] does not exist", accountNumber));
            return SUCCESS;
        };
    }

    /**
     * Function that removes all the clients in the database.
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Supplier
     * @see Status
     */
    @Override
    public Supplier<Status> removeAllClients() {
        CLIENTS.clear();
        return () -> CLIENTS.isEmpty() ? SUCCESS : ERROR;
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
        return (accountNumber, status) -> {
            var result = getClientByAccountNumber().apply(accountNumber)
                    .map(client -> {
                        client.setLocked(status);
                        return client.isLocked() == status ? SUCCESS : ERROR;
                    });
            return result.orElse(ERROR);
        };
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
        return (accountNumber, savings) -> {
            var status = getClientByAccountNumber().apply(accountNumber)
                    .map(c -> {
                        c.setSavings(savings);
                        return c.savings() == savings ? SUCCESS : ERROR;
                    });
            return status.orElse(ERROR);
        };
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
        return client -> CLIENTS.put(client.accountNumber(), client) == client ? SUCCESS : ERROR;
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
        return clients -> {
            CLIENTS.putAll(
                    clients.stream()
                            .collect(Collectors.toMap(Client::accountNumber, Function.identity()))
            );
            System.out.println(CLIENTS);
            return CLIENTS.size() == clients.size() ? SUCCESS : ERROR;
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
        return loan -> {
            var loanCount = getLoanCount().apply(loan.accountNumber());
            LOANS.put(loan.accountNumber(), loan);
            return Objects.equals(loanCount, getLoanCount().apply(loan.accountNumber())) ? SUCCESS : ERROR;
        };
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
        return () -> LOANS.values()
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
        return (loanNumber, accountNumber) -> LOANS.values()
                .stream()
                .filter(loan -> loan.loanNumber() == loanNumber && loan.accountNumber().equals(accountNumber))
                .findAny();
    }

    /**
     * Function that gets the latest loan count of a client and returns the count.
     * The function takes a {@code String} containing the account number that holds a loan.
     * @return a {@code Integer} containing the loan count of a client.
     * @see Function
     */
    @Override
    public Function<String, Integer> getLoanCount() {
        return accountNumber -> {
            int result = LOANS.values()
                    .stream()
                    .filter(loan -> loan.accountNumber().equals(accountNumber))
                    .map(Loan::loanNumber)
                    .max(Comparator.comparingInt(Integer::intValue))
                    .orElse(0);
            return result >= 1 ? result + 1 : 1;
        };
    }

    /**
     * Function that approves a loan request.
     * The function takes a {@code Loan} object containing the loan information to be approved.
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see BiFunction
     * @see Loan
     * @see Status
     */
    @Override
    public BiFunction<Loan, Client, Status> approveLoan() {
        return (loan, c) -> {
            var client = getClientByAccountNumber().apply(c.accountNumber());
            var status = updateClientSavingsByAccountNumber().apply(client.map(Client::accountNumber).orElse(null), client.map(Client::savings).orElse(0.0) + loan.amount());
            LOANS.get(loan.accountNumber()).setPending(false);
            return status;
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
        return loan -> {
            LOANS.get(loan.accountNumber()).setDeclined(true);
            return LOANS.get(loan.accountNumber()).isDeclined() ? SUCCESS : ERROR;
        };
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
        return loan -> loan.equals(LOANS.remove(loan.accountNumber())) ? SUCCESS : ERROR;
    }

    /**
     * Function that removes all the loans from the database.
     * @return a {@code Status} of the query whether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Supplier
     * @see Status
     */
    @Override
    public Supplier<Status> removeAllLoans() {
        LOANS.clear();
        return () -> LOANS.isEmpty() ? SUCCESS : ERROR;
    }

}
