package io.github.pitzzahh.entity;

import java.util.Currency;
import java.text.NumberFormat;
import java.time.format.FormatStyle;
import java.time.format.DateTimeFormatter;
import static com.github.pitzzahh.utilities.classes.TextColors.*;

/**
 * record used to generate message.
 * @param loan the loan request.
 * @param client the client who made the loan.
 */
public record Message(Loan loan, Client client) {

    /**
     * {@code NumberFormat} object for formatting numbers.
     */
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    @Override
    public String toString() {

        return String.format(CYAN_BOLD +
                "Dear %s,                                                                                " + "\n\n" + CYAN_BOLD +
                "    We have received your loan request of %s, on %s."                                     + "\n"   + CYAN_BOLD +
                "The Loan amount has been added to your savings. You must pay it before %s, "              + "\n"   + CYAN_BOLD +
                "or else there will be problems that may occur in your account."                           + "\n"   + CYAN_BOLD +
                "\n\n" +
                "                                                                      Sincerly          " + "\n"   + RED_BOLD_BRIGHT +
                "                                                                      Admin             " + RESET,
                YELLOW_BOLD + client.details().getFirstName().concat(" " + client.details().getLastName()) + RESET,
                PURPLE_BOLD + Currency.getInstance("PHP").getSymbol().concat(NUMBER_FORMAT.format(loan.amount())) + RESET,
                BLUE_BOLD + loan.dateOfLoan().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))  +RESET,
                RED_BOLD_BRIGHT + loan.dateOfLoan().plusMonths(2).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)) + RESET
        );
    }
}
