package io.github.pitzzahh.atm.entity;

import java.util.Objects;
import java.util.Currency;
import java.text.NumberFormat;
import java.time.format.FormatStyle;
import java.time.format.DateTimeFormatter;
import static com.github.pitzzahh.utilities.classes.TextColors.*;

/**
 * record used to generate message.
 * @see Loan
 * @see Client
 */
public final class Message {

    /**
     * {@code NumberFormat} object for formatting numbers.
     */
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
    private final Loan loan;
    private final Client client;
    private final boolean status;

    /**
     * @param loan   the loan request.
     * @param client the client who made the loan.
     * @param status the status of the loan if approved or declines, if {@code true},
     *               then the loan is declined and the message should fit the status.
     */
    public Message(Loan loan, Client client, boolean status) {
        this.loan = loan;
        this.client = client;
        this.status = status;
    }

    /**
     * Creates a {@code String} representation of the message.
     * @return a {@code String} containing the message.
     */
    @Override
    public String toString() {

        return status ?
                String.format(CYAN_BOLD +
                                "Dear %s,                                                                                " + "\n\n" + CYAN_BOLD +
                                "    We got your loan request of %s, on %s." + "\n" + CYAN_BOLD +
                                "Your loan request was %srefused%s, however, because you still have an outstanding " + "\n" + CYAN_BOLD +
                                "loan request that hasn't been paid. Please pay your loans to reduce loan limits." + "\n" + CYAN_BOLD +
                                "Thank you very much." + "\n" + CYAN_BOLD +
                                "\n\n" +
                                "                                                                      Sincerely          " + "\n" + RED_BOLD_BRIGHT +
                                "                                                                      Admin             " + RESET,
                        YELLOW_BOLD + client.details().getFirstName().concat(" " + client.details().getLastName()) + RESET,
                        PURPLE_BOLD + Currency.getInstance("PHP").getSymbol().concat(NUMBER_FORMAT.format(loan.amount())) + RESET,
                        BLUE_BOLD + loan.dateOfLoan().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)) + RESET,
                        RED_BOLD_BRIGHT,
                        CYAN_BOLD
                )
                :
                String.format(CYAN_BOLD +
                                "Dear %s,                                                                                " + "\n\n" + CYAN_BOLD +
                                "    We have received your loan request of %s, requested on %s." + "\n" + CYAN_BOLD +
                                "The Loan amount has been added to your balance. You must pay it before %s, " + "\n" + CYAN_BOLD +
                                "or else there will be problems that may occur in your account. Thank you very much." + "\n" + CYAN_BOLD +
                                "\n\n" +
                                "                                                                      Sincerely          " + "\n" + RED_BOLD_BRIGHT +
                                "                                                                      Admin             " + RESET,
                        YELLOW_BOLD + client.details().getFirstName().concat(" " + client.details().getLastName()) + RESET,
                        PURPLE_BOLD + Currency.getInstance("PHP").getSymbol().concat(NUMBER_FORMAT.format(loan.amount())) + RESET,
                        BLUE_BOLD + loan.dateOfLoan().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)) + RESET,
                        RED_BOLD_BRIGHT + loan.dateOfLoan().plusMonths(2).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)) + RESET
                );
    }

    /**
     * Get the loan of the client.
     * @return a {@code Loan} object.
     */
    public Loan loan() {
        return loan;
    }

    /**
     * Get the client taking the loan
     * @return {@code Client} object.
     */
    public Client client() {
        return client;
    }

    public boolean status() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Message) obj;
        return Objects.equals(this.loan, that.loan) &&
                Objects.equals(this.client, that.client) &&
                this.status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(loan, client, status);
    }

}
