package io.github.pitzzahh.dao;

import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import io.github.pitzzahh.entity.Client;
import com.github.pitzzahh.utilities.classes.enums.Status;

/**
 * interface used to separate logic on how to access the product, some and data accessing operations.
 */
public interface ClientDAO {

    Consumer<DataSource> setDataSource();

    Supplier<Map<String, Client>> getAllClients();

    Function<String, Optional<Client>> getClientByAccountNumber();

    Function<String, Status> removeClientByAccountNumber();

    BiFunction<String, String, Status> updateClientAttemptsByAccountNumber();

    BiFunction<String, Double, Status> updateClientSavingsByAccountNumber();

    Function<Client, Status> saveClient();

}