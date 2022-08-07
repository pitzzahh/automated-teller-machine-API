package io.github.pitzzahh.entity;

import java.text.NumberFormat;
import java.time.LocalDate;

/**
 * record for loan history.
 * @param loanNumber the loan number containing the current count of the loan.
 * @param accountNumber the account number that made the loan.
 * @param dateOfLoan the date when the loan is processed.
 * @param amount the amount that was loan.
 * @param pending if the loan is not yet resolved.
 */
public record Loan(int loanNumber, String accountNumber, LocalDate dateOfLoan, double amount, boolean pending, boolean isDeclined) {

    /**
     * {@code NumberFormat} object for formatting numbers.
     */
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    /**
     * record for making a loan.
     * @param accountNumber the account number that made the loan.
     * @param dateOfLoan the date when the loan is processed.
     * @param amount the amount that was loan.
     * @param loanCount the count on how many times the account made a loan.
     */
    public Loan(String accountNumber, LocalDate dateOfLoan, double amount, boolean pending) {
        this(0, accountNumber, dateOfLoan, amount, pending, false);
    }

    /**
     * record for making a loan.
     * @param loanNumber the loan number containing the current count of the loan.
     * @param accountNumber the account number that made the loan.
     * @param amount the amount that was loan.
     * @param pending if the loan is not yet resolved.
     */
    public Loan(int loanNumber, String accountNumber, double amount, boolean pending) {
        this(loanNumber, accountNumber, null, amount, pending, false);
    }

    /**
     * record for making a loan.
     * @param loanNumber the loan number containing the current count of the loan.
     * @param accountNumber the account number that made the loan.
     * @param pending if the loan is not yet resolved.
     */
    public Loan(int loanNumber, String accountNumber, boolean pending) {
        this(loanNumber, accountNumber, null, 0, pending, false);
    }

    /**
     * record for making a loan.
     * Empty {@code Loan} object.
     */
    public Loan() {
        this(0, "0", null, 0, false, false);
    }

    @Override
    public String toString() {
        return "LOAN NUMBER   : " + loanNumber                   + "\n" +
               "ACCOUNT NUMBER: " + accountNumber                + "\n" +
               "DATE OF LOAN  : " + dateOfLoan                   + "\n" +
               "AMOUNT        : " + NUMBER_FORMAT.format(amount) + "\n";
    }
}
