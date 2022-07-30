package io.github.pitzzahh.entity;

import java.time.LocalDate;

/**
 * record for loans.
 * @param loanNumber the loan number.
 * @param accountNumber the account number that made the loan.
 * @param dateOfLoan the date when the loan is processed.
 * @param amount the amount that was loan.
 * @param loanCount the count on how many times the account made a loan.
 */
public record Loan(int loanNumber, String accountNumber, LocalDate dateOfLoan, double amount, int loanCount) {}
