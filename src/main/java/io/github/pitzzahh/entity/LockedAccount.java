package io.github.pitzzahh.entity;

import com.github.pitzzahh.utilities.classes.enums.Gender;
import static com.github.pitzzahh.utilities.classes.TextColors.*;

/**
 * record used for making locked accounts.
 * @param accountNumber the account number of a locked account.
 * @param name the name of the person with the locked account.
 * @param gender the gender of the person.
 */
public record LockedAccount(String accountNumber, String name, Gender gender) {
    @Override
    public String toString() {
        return "ACCOUNT NUMBER: " + BLUE_BOLD_BRIGHT   + this.accountNumber() + "\n" +
               "NAME          : " + PURPLE_BOLD_BRIGHT + this.name()          + "\n" +
               "GENDER        : " + YELLOW_BOLD_BRIGHT + this.gender().name() + "\n" + RESET;
    }
}
