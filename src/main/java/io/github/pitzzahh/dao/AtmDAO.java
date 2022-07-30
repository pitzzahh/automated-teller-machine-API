package io.github.pitzzahh.dao;

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

/**
 * interface used to separate logic on how to access the product, some and data accessing operations.
 */
public interface AtmDAO {

    Consumer<DataSource> setDataSource();

    Supplier<Map<String, Client>> getAllClients();

    Function<String, Optional<Client>> getClientByAccountNumber();

    Function<String, Status> removeClientByAccountNumber();

    BiFunction<String, Boolean, Status> updateClientAttemptsByAccountNumber();

    BiFunction<String, Double, Status> updateClientSavingsByAccountNumber();

    Function<Loan, Status> loan();

    BiFunction<String, Double, Status> updateCurrentLoan();

    Function<String, Status> removeLoan();

    Function<Client, Status> saveClient();

}