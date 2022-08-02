package io.github.pitzzahh.entity;

/**
 * record used to make requests.
 * @param loan_number the loan number.
 * @param accountNumber the account number that requested a loan.
 * @param isApproved {@code true} if the loan is pending, otherwise false.
 */
public record Request(int loan_number, String accountNumber, double amount, boolean isApproved) { }
