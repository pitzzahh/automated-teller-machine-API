package io.github.pitzzahh.atm.exceptions;

/**
 * Exception thrown when loan does not exist.
 */
public class LoanDoesNotExist extends RuntimeException {

    public LoanDoesNotExist() {
        super("Loan does not exist");
    }

    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message.
     */
    public LoanDoesNotExist(String message) {
        super(message);
    }
}
