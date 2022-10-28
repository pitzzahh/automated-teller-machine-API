package io.github.pitzzahh.atm.entity;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Objects;

/**
 * record for loan history.
 * @see LocalDate
 */
public final class Loan {

    /**
     * {@code NumberFormat} object for formatting numbers.
     */
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
    private int loanNumber;
    private String accountNumber;
    private LocalDate dateOfLoan;
    private double amount;
    private boolean pending;
    private boolean isDeclined;

    /**
     * @param loanNumber    the loan number containing the current count of the loan.
     * @param accountNumber the account number that made the loan.
     * @param dateOfLoan    the date when the loan is processed.
     * @param amount        the amount that was loan.
     * @param pending       if the loan is not yet resolved.
     * @param isDeclined    if {@code true} the loan is declined, otherwise {@code false}.
     */
    public Loan(int loanNumber, String accountNumber, LocalDate dateOfLoan, double amount, boolean pending, boolean isDeclined) {
        this.loanNumber = loanNumber;
        this.accountNumber = accountNumber;
        this.dateOfLoan = dateOfLoan;
        this.amount = amount;
        this.pending = pending;
        this.isDeclined = isDeclined;
    }

    /**
     * record for making a loan.
     *
     * @param accountNumber the account number that made the loan.
     * @param dateOfLoan    the date when the loan is processed.
     * @param amount        the amount that was loan.
     * @param pending       if the loan is not yet resolved.
     */
    public Loan(String accountNumber, LocalDate dateOfLoan, double amount, boolean pending) {
        this(0, accountNumber, dateOfLoan, amount, pending, false);
    }

    /**
     * record for making a loan.
     *
     * @param loanNumber    the loan number containing the current count of the loan.
     * @param accountNumber the account number that made the loan.
     * @param pending       if the loan is not yet resolved.
     * @param isDeclined    if loan is declined.
     */
    public Loan(int loanNumber, String accountNumber, boolean pending, boolean isDeclined) {
        this(loanNumber, accountNumber, null, 0, pending, isDeclined);
    }

    /**
     * record for making a loan.
     *
     * @param loanNumber    the loan number containing the current count of the loan.
     * @param accountNumber the account number that made the loan.
     * @param amount        the amount that was loan.
     * @param pending       if the loan is not yet resolved.
     */
    public Loan(int loanNumber, String accountNumber, double amount, boolean pending) {
        this(loanNumber, accountNumber, null, amount, pending, false);
    }

    /**
     * record for making a loan.
     *
     * @param loanNumber    the loan number containing the current count of the loan.
     * @param accountNumber the account number that made the loan.
     * @param pending       if the loan is not yet resolved.
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

    /**
     * Creates a {@code String} representation of a loan.
     * @return a {@code String} info about the loan.
     */
    @Override
    public String toString() {
        return "LOAN NUMBER   : " + loanNumber + "\n" +
               "ACCOUNT NUMBER: " + accountNumber + "\n" +
               "DATE OF LOAN  : " + dateOfLoan + "\n" +
               "AMOUNT        : " + NUMBER_FORMAT.format(amount) + "\n";
    }

    /**
     * Get the loan number of a loan.
     * @return an {@code int} containing the loan number.
     */
    public int loanNumber() {
        return loanNumber;
    }

    /**
     * Get the account number of a client taking the loan.
     * @return a {@code String} containing the account number.
     */
    public String accountNumber() {
        return accountNumber;
    }

    /**
     * Get the date of loan.
     * @return a {@code LocalDate} object containing the date of the loan.
     * @see LocalDate
     */
    public LocalDate dateOfLoan() {
        return dateOfLoan;
    }

    /**
     * Get the amount of the loan.
     * @return a {@code double} containing the amount of the loan.
     */
    public double amount() {
        return amount;
    }

    /**
     * Get the status of the loan if pending or approved.
     * @return a {@code boolean} value if a loan is approved or pending.
     */
    public boolean pending() {
        return pending;
    }

    /**
     * Get the status of the loan if declined.
     * @return a {@code boolean} value if a loan is declined.
     */
    public boolean isDeclined() {
        return isDeclined;
    }

    /**
     * Set the loan number of a loan.
     * @param loanNumber an {@code int} containing the loan number.
     */
    public void setLoanNumber(int loanNumber) {
        this.loanNumber = loanNumber;
    }

    /**
     * Set the account number of a client taking the loan.
     * @param accountNumber a {@code String} containing the account number.
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Set the date of loan.
     * @param dateOfLoan a {@code LocalDate} object containing the date of the loan.
     * @see LocalDate
     */
    public void setDateOfLoan(LocalDate dateOfLoan) {
        this.dateOfLoan = dateOfLoan;
    }

    /**
     * Set the amount of the loan.
     * @param amount a {@code double} containing the amount of the loan.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Set the status of the loan if pending or approved.
     * @param pending a {@code boolean} value if a loan is approved or pending.
     */
    public void setPending(boolean pending) {
        this.pending = pending;
    }

    /**
     * Set the status of the loan if declined.
     * @param declined a {@code boolean} value if a loan is declined.
     */
    public void setDeclined(boolean declined) {
        isDeclined = declined;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Loan) obj;
        return this.loanNumber == that.loanNumber &&
                Objects.equals(this.accountNumber, that.accountNumber) &&
                Objects.equals(this.dateOfLoan, that.dateOfLoan) &&
                Double.doubleToLongBits(this.amount) == Double.doubleToLongBits(that.amount) &&
                this.pending == that.pending &&
                this.isDeclined == that.isDeclined;
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanNumber, accountNumber, dateOfLoan, amount, pending, isDeclined);
    }

}
