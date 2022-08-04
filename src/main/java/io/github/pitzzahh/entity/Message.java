package io.github.pitzzahh.entity;

import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Currency;

/**
 * record used to generate message.
 * @param loan the loan loan.
 * @param client the client
 */
public record Message(Loan loan, Client client) {
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
    @Override
    public String toString() {

        var cal = Calendar.getInstance();
        cal.setTime(Date.valueOf(loan.dateOfLoan()));
        cal.add(Calendar.MONTH, 2);

        return String.format(
                "Dear %s,                                                                                " + "\n\n" +
                "    We have received your loan request of %s, on %s."                                     + "\n"   +
                "The Loan amount has been added to your savings. You must pay it before %s, "              + "\n"   +
                "or else there will be problems that may occur in your account."                           + "\n"   +
                "\n\n" +
                "                                                                      Sincerly          " + "\n"   +
                "                                                                      Admin             ",
                client.details().getFirstName().concat(" " + client.details().getLastName()),
                Currency.getInstance("PHP").getSymbol().concat(NUMBER_FORMAT.format(loan.amount())),
                loan.dateOfLoan().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)),
                LocalDateTime.ofInstant(cal.getTime().toInstant(), ZoneId.of("GMT")).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
        );
    }
}
