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
import io.github.pitzzahh.service.AtmService;
import com.github.pitzzahh.utilities.classes.enums.Status;

/**
 * interface used to access the database. Implemented by {@code AtmDAOImplementation}.
 * Used in {@code AtmService}
 * @see AtmDAOImplementation
 * @see AtmService
 */
public interface AtmDAO {

    /**
     * Function that accepts a {@code DataSource} object.
     * Object needed to connect to the database.
     * @see Consumer
     * @see DataSource
     */
    Consumer<DataSource> setDataSource();

    /**
     * Function that suppplies a {@code Map<String, Client>}.
     * <p>{@code String} - the key, the key is the account number of the client</p>
     * <p>{@code Client} - the value, the value is the client object</p>
     * @return a {@code Client} object
     * @see Map
     * @see Supplier
     * @see Client
     */
    Supplier<Map<String, Client>> getAllClients();

    /**
     * Function that returns a {@code Optional<Client>} object based on the {@code String}
     * that contains the account number.
     * <p>T - a {@code String} containing the account number to be search from the database.</p>
     * <p>R - a {@code Optional<Client>} containing the result if the client is found or not.</p>
     * @return a {@code Optional<Client>} object.
     * @see Function
     * @see Optional
     * @see Client
     */
    Function<String, Optional<Client>> getClientByAccountNumber();

    /**
     * Function that accepts a {@code String} containing the account number.
     * The account number will be used to search for the clients savings.
     * @return a {@code Double} containing the savings of the client with the account number.
     * @see Function
     */
    Function<String, Double> getClientSavingsByAccountNumber();

    /**
     * Function that removes a client in the database using the account number.
     * <p>T - a {@code String} the account number of the client needed in order to remove the client.</p>
     * <p>R - the {@code Status} of the operation if {@link Status#SUCCESS} or {@link Status#ERROR}.</p>
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.</p>
     * @see Function
     * @see Status
     */
    Function<String, Status> removeClientByAccountNumber();

    /**
     * Function that removes all the clients in the database.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.</p>
     * @see Supplier
     * @see Status
     */
    Supplier<Status> removeAllClients();

    /**
     * Function that accepts two values. A {@code String} and a {@code Boolean}.
     * <p>First parameter is a {@code String} contains the account number of the client.</p>
     * <p>Second parameter is a {@code Boolean}, {@code true} if the client account should be locked, default is false.</p>
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.</p>
     * @see BiFunction
     * @see Status
     */
    BiFunction<String, Boolean, Status> updateClientStatusByAccountNumber();

    /**
     * Function that accepts two values. A {@code String} and a {@code Double}.
     * <p>First parameter is a {@code String} contains the account number of the client.</p>
     * <p>Second parameter is a {@code Double}, the new savings balance of the client.</p>
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.</p>
     * @see BiFunction
     * @see Status
     */
    BiFunction<String, Double, Status> updateClientSavingsByAccountNumber();

    /**
     * Function that save a client to the database. The function takes a {@code Client} object,
     * the object to be saved in the database table.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.</p>
     * @see Function
     * @see Client
     * @see Status
     */
    Function<Client, Status> saveClient();

    /**
     * Function that saves a {@code Collection<Client} to the database table.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.</p>
     * @see Function
     * @see Collection
     * @see Client
     * @see Status
     */
    Function<Collection<Client>, Status> saveAllClients();

    /**
     * Function that submits a loan request.
     * The Function takes a {@code Loan} object containing the loan information.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.</p>
     * @see Function
     * @see Loan
     * @see Status
     */
    Function<Loan, Status> requestLoan();

    /**
     * Function that returns a key value pair, a Map<K,V> in particular.
     * <p>K - is a {@code String} containing the key, the key is the account number of the client who requested a loan.</p>
     * <p>V - is a {@code String} containing the value, the value is all the loans that the account requested. It is a {@code List<Loan>}</p>
     * @return a {@code Map<String, List<Loan>>} a key value pair containing all the loans from the table in the database.
     * @see Supplier
     * @see Map
     * @see List
     * @see Loan
     */
    Supplier<Map<String, List<Loan>>> getAllLoans();

    /**
     * Function that gets the loan of a client using loan number and account number.
     * The function takes an {@code Integer} and a {@code String}, the integer containing the loan number,
     * and the string containing the account number.
     * @return an {@code Optional<Loan>} wether the loan exist or not.
     * @see BiFunction
     * @see Optional
     * @see Loan
     */
    BiFunction<Integer, String, Optional<Loan>> getLoanByLoanNumberAndAccountNumber();

    /**
     * Function that gets the latest loan count of a client and returns the count.
     * The function takes a {@code String} containing the account number that holds a loan.
     * @return a {@code Integer} containing the loan count of a client.
     * @see Function
     */
    Function<String, Integer> getLoanCount();

    /**
     * Function that approves a loan request.
     * The function takes a {@code Loan} object containing the loan information to be approved.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.</p>
     * @see Function
     * @see Loan
     * @see Status
     */
    Function<Loan, Status> approveLoan();

    /**
     * Function that removes a loan.
     * The function takes a {@code Loan} object containing the loan information to be removed.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.</p>
     * @see Function
     * @see Loan
     * @see Status
     */
    Function<Loan, Status> removeLoan();

    /**
     * Function that removes all the loans from the database.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.</p>
     * @see Supplier
     * @see Status
     */
    Supplier<Status> removeAllLoans();

    /**
     * Function that gets the message of the loan requst of a client to the database.
     * The Function takes an {@code Integer} and a {@code String}.
     * The {@code Integer} contains the loan number of the requested loan.
     * The {@code String} contains the account number of the client.
     * @return a {@code String} object containg the message of the loan.
     * @see BiFunction
     */
    BiFunction<Integer, String, Map<String, List<String>>> getMessage();
}