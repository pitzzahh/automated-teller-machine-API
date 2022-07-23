package io.github.pitzzahh.entity;

import java.text.NumberFormat;
import com.github.pitzzahh.utilities.classes.Person;
import static com.github.pitzzahh.utilities.classes.TextColors.*;

public record Client(String accountNumber, String pin, Person details, double savings, int attempts) {
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
    @Override
    public String toString() {
        return "ACCOUNT NUMBER: " + BLUE_BOLD + this.accountNumber()                                                      + "\n" +
               "ACCOUNT PIN   : " + RED_BOLD + this.pin()                                                                 + "\n" +
               "FIRST NAME    : " + PURPLE_BOLD + this.details().getFirstName()                                           + "\n" +
               "LAST NAME     : " + GREEN_BOLD + this.details().getLastName()                                             + "\n" +
               "AGE           : " + YELLOW_BOLD + this.details().getAge()                                                 + "\n" +
               "GENDER        : " + BLUE_BOLD + this.details().getGender().name()                                         + "\n" +
               "ADDRESS       : " + RED_BOLD + this.details().getAddress()                                                + "\n" +
               "BIRTH DATE    : " + PURPLE_BOLD + this.details().getBirthDate().toString() + RESET                        + "\n" +
               "SAVINGS       : " + YELLOW_BOLD_BRIGHT + "₱ " + GREEN_BOLD + NUMBER_FORMAT.format(this.savings()) + RESET + "\n";
    }
}
