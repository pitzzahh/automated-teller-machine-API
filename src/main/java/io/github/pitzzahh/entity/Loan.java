package io.github.pitzzahh.entity;

import java.time.LocalDate;

/**
 * record for loan history.
 * @param accountNumber the account number that made the loan.
 * @param dateOfLoan the date when the loan is processed.
 * @param amount the amount that was loan.
 * @param loanCount the count on how many times the account made a loan.
 */
public record Loan(int loanNumber, String accountNumber, LocalDate dateOfLoan, double amount, boolean pending) {

    public Loan(String accountNumber, LocalDate dateOfLoan, double amount, boolean pending) {
        this(0, accountNumber, dateOfLoan, amount, pending);
    }

    @Override
    public String toString() {
        return "\nLOAN NUMBER : " + loanNumber    + "\n" +
               "ACCOUNT NUMBER: " + accountNumber + "\n" +
               "DATE OF LOAN  : " + dateOfLoan    + "\n" +
               "AMOUNT        : " + amount        + "\n" +
               "PENDING       : " + pending       + "\n";
    }
}
