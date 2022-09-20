package io.github.pitzzahh.atm.entity;

import java.util.Objects;
import java.util.Currency;
import java.text.NumberFormat;
import com.github.pitzzahh.utilities.classes.Person;
import static com.github.pitzzahh.utilities.classes.TextColors.*;

/**
 * record used for making clients.
 */
public final class Client {

    /**
     * {@code NumberFormat} object for formatting numbers.
     */
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
    private final String accountNumber;
    private final String pin;
    private final Person details;
    private final double savings;
    private final boolean isLocked;

    /**
     * @param accountNumber the account number, denoted by a 9-digit number.
     * @param pin           the pin for the account, denoted by a 6-digit number.
     * @param details       a {@code Person} object containing the details of the person with an account.
     * @param savings       the current savings in this account.
     * @param isLocked      {@code true} if the account is locked, otherwise false.
     */
    public Client(String accountNumber, String pin, Person details, double savings, boolean isLocked) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.details = details;
        this.savings = savings;
        this.isLocked = isLocked;
    }

    /**
     * Empty client object.
     */
    public Client() {
        this("", "", null, 0, false);
    }

    /**
     * Constructs a {@code String} representation of a a {@code Client}.
     * @return a {@code String} representation of a {@code Client}.
     */
    @Override
    public String toString() {
        return "ACCOUNT NUMBER: " + BLUE_BOLD + this.accountNumber() + "\n" +
                "ACCOUNT PIN   : " + RED_BOLD + this.pin() + "\n" +
                "FIRST NAME    : " + PURPLE_BOLD + this.details().getFirstName() + "\n" +
                "LAST NAME     : " + GREEN_BOLD + this.details().getLastName() + "\n" +
                "AGE           : " + YELLOW_BOLD + this.details().getAge() + "\n" +
                "GENDER        : " + BLUE_BOLD + String.join(" ", this.details().getGender().name().split("_")) + "\n" +
                "ADDRESS       : " + RED_BOLD + this.details().getAddress() + "\n" +
                "BIRTH DATE    : " + PURPLE_BOLD + this.details().getBirthDate().toString() + "\n" +
                "BALANCE       : " + YELLOW_BOLD_BRIGHT + Currency.getInstance("PHP").getSymbol().concat(NUMBER_FORMAT.format(this.savings)) + RESET + "\n";
    }

    /**
     * Returns the account number.
     * @return the account number of a {@code Client}.
     */
    public String accountNumber() {
        return accountNumber;
    }

    /**
     * Returns the pin.
     * @return the pin of a {@code Client}.
     */
    public String pin() {
        return pin;
    }

    /**
     * Returns the details of a client.
     * @return a {@code Person} object containing the details of the client.
     * @see Person
     */
    public Person details() {
        return details;
    }

    /**
     * Returns the savings.
     * @return the savings of a client.
     */
    public double savings() {
        return savings;
    }

    /**
     * Returns the status of the account of a client if locked or not.
     * @return the status of the account of a client if locked.
     */
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Client) obj;
        return Objects.equals(this.accountNumber, that.accountNumber) &&
                Objects.equals(this.pin, that.pin) &&
                Objects.equals(this.details, that.details) &&
                Double.doubleToLongBits(this.savings) == Double.doubleToLongBits(that.savings) &&
                this.isLocked == that.isLocked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, pin, details, savings, isLocked);
    }

}
