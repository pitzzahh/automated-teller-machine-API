package io.github.pitzzahh.atm.service;

import java.util.*;
import javax.sql.DataSource;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.BiFunction;
import io.github.pitzzahh.atm.dao.AtmDAO;
import io.github.pitzzahh.atm.database.DatabaseConnection;
import io.github.pitzzahh.atm.entity.Loan;
import io.github.pitzzahh.atm.entity.Client;
import io.github.pitzzahh.atm.entity.Message;
import io.github.pitzzahh.atm.dao.AtmDAOImplementation;
import com.github.pitzzahh.utilities.classes.enums.Status;

/**
 * The service for overall functionality..
 */
public class AtmService {

    /**
     * {@code AtmDAO} dependency.
     */
    private AtmDAO atmDAO;

    /**
     * {@code DatabaseConnection} dependency.
     */
    public DatabaseConnection connection;

    /**
     * Dependency ibjection object.
     * @param atmDAO the {@code AtmDAO} dependency to be injected.
     * @param connection the {@code DatabaseConnection} dependency to be injected.
     * @see AtmDAO
     */
    public AtmService(AtmDAO atmDAO, DatabaseConnection connection) {
        this.atmDAO = new AtmDAOImplementation();
        this.connection = new DatabaseConnection();
    }

    /**
     * Function that accepts a {@code DataSource} object.
     * Object needed to connect to the database.
     * @return nothing
     * @see Consumer
     * @see DataSource
     */
    public Consumer<DataSource> setDataSource() {
        return atmDAO.setDataSource();
    }

    /**
     * Function that suppplies a {@code Map<String, Client>}.
     * <p>{@code String} - the key, the key is the account number of the client</p>
     * <p>{@code Client} - the value, the value is the client object</p>
     * @return a {@code Client} object
     * @see Map
     * @see Supplier
     * @see Client
     */
    public Supplier<Map<String, Client>> getAllClients() {
        return atmDAO.getAllClients();
    }

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
    public Function<String, Optional<Client>> getClientByAccountNumber() {
        return atmDAO.getClientByAccountNumber();
    }

    /**
     * Function that accepts a {@code String} containing the account number.
     * The account number will be used to search for the clients savings.
     * @return a {@code Double} containing the savings of the client with the account number.
     * @see Function
     */
    public Function<String, Double> getClientSavingsByAccountNumber() {
        return atmDAO.getClientSavingsByAccountNumber();
    }

    /**
     * Function that removes a client in the database using the account number.
     * <p>T - a {@code String} the account number of the client needed in order to remove the client.</p>
     * <p>R - the {@code Status} of the operation if {@link Status#SUCCESS} or {@link Status#ERROR}.</p>
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Function
     * @see Status
     */
    public Function<String, Status> removeClientByAccountNumber() {
        return atmDAO.removeClientByAccountNumber();
    }

    /**
     * Function that removes all the clients in the database.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Supplier
     * @see Status
     */
    public Supplier<Status> removeAllClients() {
        return atmDAO.removeAllClients();
    }

    /**
     * Function that accepts two values. A {@code String} and a {@code Boolean}.
     * <p>First parameter is a {@code String} contains the account number of the client.</p>
     * <p>Second parameter is a {@code Boolean}, {@code true} if the client account should be locked, default is false.</p>
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see BiFunction
     * @see Status
     */
    public BiFunction<String, Boolean, Status> updateClientAccountStatusByAccountNumber() {
        return atmDAO.updateClientStatusByAccountNumber();
    }

    /**
     * Function that accepts two values. A {@code String} and a {@code Double}.
     * <p>First parameter is a {@code String} contains the account number of the client.</p>
     * <p>Second parameter is a {@code Double}, the new savings balance of the client.</p>
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see BiFunction
     * @see Status
     */
    public BiFunction<String, Double, Status> updateClientSavingsByAccountNumber() {
        return atmDAO.updateClientSavingsByAccountNumber();
    }

    /**
     * Function that save a client to the database. The function takes a {@code Client} object,
     * the object to be saved in the database table.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Function
     * @see Client
     * @see Status
     */
    public Function<Client, Status> saveClient() {
        return atmDAO.saveClient();
    }

    /**
     * Function that saves a {@code Collection<Client} to the database table.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Function
     * @see Collection
     * @see Client
     * @see Status
     */
    public Function<Collection<Client>, Status> saveAllClients() {
        return atmDAO.saveAllClients();
    }

    /**
     * Function that submits a loan request.
     * The Function takes a {@code Loan} object containing the loan information.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Function
     * @see Loan
     * @see Status
     */
    public Function<Loan, Status> requestLoan() {
        return atmDAO.requestLoan();
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
    public Supplier<Map<String, List<Loan>>> getAllLoans() {
        return atmDAO.getAllLoans();
    }

    /**
     * Function that gets the loan of a client using loan number and account number.
     * The function takes an {@code Integer} and a {@code String}, the integer containing the loan number,
     * and the string containing the account number.
     * @return an {@code Optional<Loan>} wether the loan exist or not.
     * @see BiFunction
     * @see Optional
     * @see Loan
     */
    public BiFunction<Integer, String, Optional<Loan>> getLoanByLoanNumberAndAccountNumber() {
        return atmDAO.getLoanByLoanNumberAndAccountNumber();
    }

    /**
     * Function that gets the latest loan count of a client and returns the count.
     * The function takes a {@code String} containing the account number that holds a loan.
     * @return a {@code Integer} containing the loan count of a client.
     * @see Function
     */
    public Function<String, Integer> getLoanCount() {
        return atmDAO.getLoanCount();
    }

    /**
     * Function that approves a loan request.
     * The function takes a {@code Loan} object containing the loan information to be approved.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see BiFunction
     * @see Loan
     * @see Status
     */
    public BiFunction<Loan, Client, Status> approveLoan() {
        return atmDAO.approveLoan();
    }

    /**
     * Function that declines a loan request.
     * The function takes a {@code Loan} object containing the loan information to be approved.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Function
     * @see Loan
     * @see Status
     */
    public Function<Loan, Status> declineLoan() {
        return atmDAO.declineLoan();
    }

    /**
     * Function that removes a loan.
     * The function takes a {@code Loan} object containing the loan information to be removed.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Function
     * @see Loan
     * @see Status
     */
    public Function<Loan, Status> removeLoan() {
        return atmDAO.removeLoan();
    }

    /**
     * Function that removes all the loans from the database.
     * @return a {@code Status} of the query wether {@link Status#SUCCESS} or {@link Status#ERROR}.
     * @see Supplier
     * @see Status
     */
    public Supplier<Status> removeAllLoans() {
        return atmDAO.removeAllLoans();
    }

    /**
     * Function that gets the message of the loan requst of a client to the database.
     * The Function takes a {@code String}.
     * The {@code String} contains the account number of the client.
     * @return a {@code Message} object containg the message of the loan.
     * @see Function
     * @see Map
     * @see List
     * @see Message
     */
    public Function<String, Map<String, List<Message>>> getMessage() {
        return atmDAO.getMessage();
    }
}
