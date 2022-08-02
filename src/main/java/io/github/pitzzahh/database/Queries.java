package io.github.pitzzahh.database;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.function.Function;
import io.github.pitzzahh.entity.Client;
import io.github.pitzzahh.entity.Loan;
import io.github.pitzzahh.entity.Request;
import io.github.pitzzahh.mapper.LoanMapper;
import io.github.pitzzahh.mapper.ClientMapper;
import com.github.pitzzahh.utilities.SecurityUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import com.github.pitzzahh.utilities.classes.enums.Status;
import static com.github.pitzzahh.utilities.classes.enums.Status.*;

public class Queries {

    public static Supplier<Map<String, Client>> getAllClientsQuery(JdbcTemplate db) {
        return () -> db.query("SELECT * FROM clients", new ClientMapper())
                .stream()
                .collect(Collectors.toMap(Client::accountNumber, Function.identity()));
    }

    public static Optional<Client> getClientByAccountNumberQuery(String $an, JdbcTemplate db) {
        final var QUERY = "SELECT * FROM clients WHERE account_number = ?";
        return Optional.ofNullable(Objects.requireNonNull(db.queryForObject(QUERY, new ClientMapper(), SecurityUtil.encrypt($an))));
    }

    public static Status removeClientByAccountNumberQuery(String $an, JdbcTemplate db) {
        return db.update("DELETE FROM clients WHERE account_number = ?", SecurityUtil.encrypt($an)) > 0 ? SUCCESS : ERROR;
    }

    public static Status removeAllClientsQuery(JdbcTemplate db) {
        return db.update("DELETE FROM clients") > 0 ? SUCCESS : ERROR;
    }

    public static Status updateClientStatusByAccountNumber(String an, boolean status, JdbcTemplate db) {
        return db.update("UPDATE clients SET isLocked = ? WHERE account_number = ?", status, SecurityUtil.encrypt(an)) > 0 ? SUCCESS : ERROR;
    }

    public static Status updateClientSavingsByAccountNumberQuery(String an, double savings, JdbcTemplate db) {
        return db.update("UPDATE clients SET savings = ? WHERE account_number = ?", SecurityUtil.encrypt(String.valueOf(savings)), SecurityUtil.encrypt(an)) > 0 ? SUCCESS : ERROR;
    }

    public static Status saveClientQuery(Client client, JdbcTemplate db) {
        final var QUERY = "INSERT INTO clients (account_number, pin, first_name, last_name, gender, address, date_of_birth, savings, isLocked) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return db.update(
                QUERY,
                SecurityUtil.encrypt(client.accountNumber()),
                SecurityUtil.encrypt(client.pin()),
                SecurityUtil.encrypt(client.details().getFirstName()),
                SecurityUtil.encrypt(client.details().getLastName()),
                client.details().getGender().toString(),
                SecurityUtil.encrypt(client.details().getAddress()),
                client.details().getBirthDate(),
                SecurityUtil.encrypt(String.valueOf(client.savings())),
                client.isLocked()
        ) > 0 ? SUCCESS : ERROR;
    }

    public static Status requestLoanQuery(Loan loan, JdbcTemplate db) {
        final var QUERY = "INSERT INTO loans(loan_number, account_number, date_of_loan, amount, pending) VALUES(?, ?, ?, ?, ?)";
        final var loanCount = getLoanCountQuery(loan.accountNumber(), db);
        return db.update(
                QUERY,
                loanCount,
                SecurityUtil.encrypt(loan.accountNumber()),
                loan.dateOfLoan(),
                SecurityUtil.encrypt(String.valueOf(loan.amount())),
                SecurityUtil.encrypt(String.valueOf(loan.pending()))
        ) > 0 ? SUCCESS : ERROR;
    }

    public static Supplier<Map<String, List<Loan>>> getAllLoansQuery(JdbcTemplate db) {
        return () -> db.query("SELECT * FROM loans", new LoanMapper())
                .stream()
                .collect(Collectors.groupingBy(Loan::accountNumber));
    }

    public static Optional<Loan> getLoanByLoanNumberAndAccountNumberQuery(int loanNumber, String $an, JdbcTemplate db) {
        final var QUERY = "SELECT * FROM loans WHERE loan_number = ? AND account_number = ?";
        return Optional.ofNullable(Objects.requireNonNull(db.queryForObject(QUERY, new LoanMapper(), loanNumber, SecurityUtil.encrypt($an))));
    }

    public static int getLoanCountQuery(String accountNumber, JdbcTemplate db) {
        final var QUERY = "SELECT MAX(loan_number) FROM loans WHERE account_number = ?";
        var result = Optional.ofNullable(db.queryForObject(QUERY, Integer.class, SecurityUtil.encrypt(accountNumber))).orElse(0);
        return result >= 1 ? result + 1 : 1;
    }

    public static Status approveLoanQuery(Request request, JdbcTemplate db) {
        final var QUERY = "UPDATE loans SET pending = ? WHERE loan_number = ? AND account_number = ?";
        return db.update(
                QUERY,
                SecurityUtil.encrypt(Boolean.TRUE.toString()),
                request.loan_number(),
                request.accountNumber()
        ) > 0 ? SUCCESS : ERROR;
    }

    public static Status removeLoanQuery(Request request, JdbcTemplate db) {
        final var QUERY = "DELETE FROM loans WHERE loan_number = ? AND account_number = ? AND pending = ?";
        return db.update(
                QUERY,
                request.loan_number(),
                request.accountNumber(),
                SecurityUtil.encrypt(Boolean.TRUE.toString())
        ) > 0 ? SUCCESS : ERROR;
    }

    public static Status removeAllLoansQuery(JdbcTemplate db) {
        return db.update("DELETE FROM loans") > 0 ? SUCCESS : ERROR;
    }
}
