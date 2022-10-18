package io.github.pitzzahh.atm.exceptions;

/**
 * Exception thrown when loan does not exist.
 */
public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException() {
        super("Loan does not exist");
    }

    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message.
     */
    public LoanNotFoundException(String message) {
        super(message);
    }
}
