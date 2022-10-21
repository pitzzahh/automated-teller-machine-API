package io.github.pitzzahh.atm.validator;

import io.github.pitzzahh.atm.dao.InMemory;
import io.github.pitzzahh.atm.dao.AtmDAO;
import java.util.function.Predicate;

/**
 * The validator for the service.
 */
public interface ServiceValidator extends Predicate<String> {

    /**
     * Function that accepts a {@code String} object.
     */
    AtmDAO inMemory = new InMemory();

    /**
     * Function that accepts a {@code String} object.
     * @return a {@code boolean} value
     */
    static ServiceValidator doesClientAlreadyExist() {
        return inMemory.getAllClients().get()::containsKey;
    }

}

