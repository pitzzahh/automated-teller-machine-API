package io.github.pitzzahh.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.BiFunction;
import io.github.pitzzahh.entity.Loan;
import io.github.pitzzahh.entity.Client;
import com.github.pitzzahh.utilities.classes.enums.Status;
import io.github.pitzzahh.entity.Request;

/**
 * interface used to separate logic on how to access the product, some and data accessing operations.
 */
public interface AtmDAO {

    Consumer<DataSource> setDataSource();

    Supplier<Map<String, Client>> getAllClients();

    Function<String, Optional<Client>> getClientByAccountNumber();

    Function<String, Status> removeClientByAccountNumber();

    Supplier<Status> removeAllClients();

    BiFunction<String, Boolean, Status> updateClientAttemptsByAccountNumber();

    BiFunction<String, Double, Status> updateClientSavingsByAccountNumber();

    Function<Client, Status> saveClient();

    Function<Collection<Client>, Status> saveAllClient();

    Function<Loan, Status> requestLoan();

    Supplier<Map<String, List<Loan>>> getAllLoans();

    BiFunction<Integer, String, Optional<Loan>> getLoanByLoanNumberAndAccountNumber();

    Function<String, Integer> getLoanCount();

    Function<Request, Status> approveLoan();

    Function<Request, Status> removeLoan();

    Supplier<Status> removeAllLoans();
}