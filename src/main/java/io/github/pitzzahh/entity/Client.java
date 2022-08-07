package io.github.pitzzahh.entity;

import java.util.Arrays;
import java.text.NumberFormat;
import java.util.stream.Collectors;
import com.github.pitzzahh.utilities.classes.Person;
import static com.github.pitzzahh.utilities.classes.TextColors.*;

/**
 * record used for making clients.
 * @param accountNumber the account number, denoted by a 9 digit number.
 * @param pin the pin for the account, denoted by a 6 digit number.
 * @param details a {@code Person} object containing the details of the person with an account.
 * @param savings the current savings in this account.
 * @param isLocked {@code true} if the account is locked, otherwise false.
 */
public record Client(String accountNumber, String pin, Person details, double savings, boolean isLocked) {

    /**
     * {@code NumberFormat} object for formatting numbers.
     */
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    /**
     * Empty client object.
     */
    public Client() {
        this("", "", null, 0, false);
    }

    @Override
    public String toString() {
        return "ACCOUNT NUMBER: " + BLUE_BOLD + this.accountNumber()                                                                                                    + "\n" +
               "ACCOUNT PIN   : " + RED_BOLD + this.pin()                                                                                                               + "\n" +
               "FIRST NAME    : " + PURPLE_BOLD + this.details().getFirstName()                                                                                         + "\n" +
               "LAST NAME     : " + GREEN_BOLD + this.details().getLastName()                                                                                           + "\n" +
               "AGE           : " + YELLOW_BOLD + this.details().getAge()                                                                                               + "\n" +
               "GENDER        : " + BLUE_BOLD + Arrays.stream(this.details().getGender().name().split("_")).collect(Collectors.joining(" "))                            + "\n" +
               "ADDRESS       : " + RED_BOLD + this.details().getAddress()                                                                                              + "\n" +
               "BIRTH DATE    : " + PURPLE_BOLD + this.details().getBirthDate().toString() + RESET                                                                      + "\n" +
               "SAVINGS       : " + YELLOW_BOLD_BRIGHT + "₱ " + GREEN_BOLD + NUMBER_FORMAT.format(this.savings()) + RESET                                               + "\n";
    }
}
