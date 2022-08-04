package io.github.pitzzahh.service;

import java.time.Clock;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.BiFunction;
import io.github.pitzzahh.dao.AtmDAO;
import io.github.pitzzahh.entity.Loan;
import io.github.pitzzahh.entity.Client;
import io.github.pitzzahh.dao.AtmDAOImplementation;
import com.github.pitzzahh.utilities.classes.enums.Status;

/**
 * The service for managing products.
 */
public class AtmService {

    private AtmDAO atmDAO;
    private Clock clock;

    public AtmService(AtmDAO atmDAO, Clock clock) {
        this.atmDAO = new AtmDAOImplementation();
        this.clock = clock;
    }

    public Consumer<DataSource> setDataSource() {
        return atmDAO.setDataSource();
    }
    /**
     * @return
     */
    public Supplier<Map<String, Client>> getAllClients() {
        return atmDAO.getAllClients();
    }

    /**
     * @return
     */
    public Function<String, Optional<Client>> getClientByAccountNumber() {
        return atmDAO.getClientByAccountNumber();
    }

    /**
     * @return
     */
    public Function<String, Status> removeClientByAccountNumber() {
        return atmDAO.removeClientByAccountNumber();
    }

    public Supplier<Status> removeAllClients() {
        return atmDAO.removeAllClients();
    }
    /**
     * @return
     */
    public BiFunction<String, Boolean, Status> updateClientAttemptsByAccountNumber() {
        return atmDAO.updateClientAttemptsByAccountNumber();
    }

    /**
     * @return
     */
    public BiFunction<String, Double, Status> updateClientSavingsByAccountNumber() {
        return atmDAO.updateClientSavingsByAccountNumber();
    }

    /**
     * @return
     */
    public Function<Client, Status> saveClient() {
        return atmDAO.saveClient();
    }

    /**
     * @return
     */
    public Function<Collection<Client>, Status> saveAllClient() {
        return atmDAO.saveAllClient();
    }

    /**
     * @return
     */
    public Function<Loan, Status> requestLoan() {
        return atmDAO.requestLoan();
    }

    /**
     * @return
     */
    public Supplier<Map<String, List<Loan>>> getAllLoans() {
        return atmDAO.getAllLoans();
    }

    /**
     * @return
     */
    public BiFunction<Integer, String, Optional<Loan>> getLoanByLoanNumberAndAccountNumber() {
        return atmDAO.getLoanByLoanNumberAndAccountNumber();
    }

    /**
     * @return
     */
    public Function<String, Integer> getLoanCount() {
        return atmDAO.getLoanCount();
    }

    /**
     * @return
     */
    public Function<Loan, Status> approveLoan() {
        return atmDAO.approveLoan();
    }

    /**
     * @return
     */
    public Function<Loan, Status> removeLoan() {
        return atmDAO.removeLoan();
    }

    /**
     * @return
     */
    public Supplier<Status> removeAllLoans() {
        return atmDAO.removeAllLoans();
    }
}
