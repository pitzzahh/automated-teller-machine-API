package io.github.pitzzahh.atm.exceptions;

/**
 * Class that represents an exception thrown when a client is not found.
 */
public class ClientNotFoundException extends RuntimeException {

    /**
     * Constructor that accepts a {@code String} containing the message to be displayed.
     * @param message the message to be displayed.
     */
    public ClientNotFoundException(String message) {
        super(message);
    }
}
