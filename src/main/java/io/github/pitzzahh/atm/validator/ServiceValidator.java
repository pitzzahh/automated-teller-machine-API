package io.github.pitzzahh.atm.validator;

import io.github.pitzzahh.atm.dao.InMemory;
import io.github.pitzzahh.atm.dao.AtmDAO;
import io.github.pitzzahh.atm.service.AtmService;

import java.util.function.Predicate;

/**
 * The validator for the service.
 */
public interface ServiceValidator extends Predicate<String> {

    /**
     * Function that accepts a {@code String} object.
     * @return a {@code boolean} value
     */
    static ServiceValidator doesClientAlreadyExist(AtmDAO dao) {
        return dao.getAllClients().get()::containsKey;
    }

}

