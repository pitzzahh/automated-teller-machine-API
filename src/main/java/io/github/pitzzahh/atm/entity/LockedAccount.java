package io.github.pitzzahh.atm.entity;

import static io.github.pitzzahh.util.utilities.classes.Colors.*;
import io.github.pitzzahh.util.utilities.classes.enums.Gender;
import java.util.Objects;

/**
 * record used for making locked accounts.
 * @see Gender
 */
public final class LockedAccount {

    private final String accountNumber;
    private final String name;
    private final Gender gender;

    /**
     * @param accountNumber the account number of a locked account.
     * @param name          the name of the person with the locked account.
     * @param gender        the gender of the person.
     */
    public LockedAccount(String accountNumber, String name, Gender gender) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.gender = gender;
    }

    /**
     * Creates a {@code String} representation of the locked account
     * @return a {@code String} containing the information about the locked account.
     */
    @Override
    public String toString() {
        return "ACCOUNT NUMBER: " + BLUE_BOLD_BRIGHT + this.accountNumber() + "\n" +
               "NAME          : " + PURPLE_BOLD_BRIGHT + this.name() + "\n" +
               "GENDER        : " + YELLOW_BOLD_BRIGHT + this.gender().name() + "\n" + RESET;
    }

    /**
     * Get the account number of a client with a locked account.
     * @return a {@code String} containing the account number.
     */
    public String accountNumber() {
        return accountNumber;
    }

    /**
     * Get the name of the client with the locked account.
     * @return a {@code String} containing the name of the client.
     */
    public String name() {
        return name;
    }

    /**
     * Get the gender of a client with a locked account.
     * @return a {@code Gender} object containing the gender.
     * @see Gender
     */
    public Gender gender() {
        return gender;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LockedAccount) obj;
        return Objects.equals(this.accountNumber, that.accountNumber) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.gender, that.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, name, gender);
    }

}
