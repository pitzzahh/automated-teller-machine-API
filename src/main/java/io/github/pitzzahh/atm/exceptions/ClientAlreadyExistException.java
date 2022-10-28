package io.github.pitzzahh.atm.exceptions;

/**
 * Exception thrown when client already exist.
 */
public class ClientAlreadyExistException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message.
     */
    public ClientAlreadyExistException(String message) {
            super(message);
        }

}
