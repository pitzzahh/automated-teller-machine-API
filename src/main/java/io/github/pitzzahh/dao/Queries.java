package io.github.pitzzahh.dao;

import java.util.*;
import java.util.function.Supplier;
import io.github.pitzzahh.entity.Client;
import io.github.pitzzahh.mapper.ClientMapper;
import com.github.pitzzahh.utilities.SecurityUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import com.github.pitzzahh.utilities.classes.enums.Status;
import org.springframework.dao.EmptyResultDataAccessException;
import static com.github.pitzzahh.utilities.classes.enums.Status.*;

// TODO: create a test for this.
// TODO: implement updateClientAttemptsByAccountNumberQuery() and updateClientSavingsByAccountNumberQuery()
public class Queries {

    public static Supplier<List<Optional<Client>>> getAllClientsQuery(JdbcTemplate jdbc) {
        return () -> jdbc.query("SELECT * FROM clients", new ClientMapper());
    }

    public static Optional<Client> getClientByAccountNumberQuery(String $an, JdbcTemplate jdbc) {
        try {
            return jdbc.queryForObject("SELECT * FROM clients WHERE account_number = ?", new Object[]{SecurityUtil.encrypt($an)}, new ClientMapper());
        } catch (EmptyResultDataAccessException ignored) {}
        return Optional.empty();
    }

    public static Status removeClientByAccountNumberQuery(String $an, JdbcTemplate jdbc) {
        return jdbc.update("DELETE FROM clients WHERE account_number = ?", $an) > 0 ? SUCCESS : ERROR;
    }

    public static Status updateClientAttemptsByAccountNumberQuery(String an, String at, JdbcTemplate jdbc) {
        return jdbc.update("UPDATE clients SET attempts = ? WHERE account_number = ?", Integer.parseInt(at), SecurityUtil.encrypt(an)) > 0 ? SUCCESS : ERROR;

    }

    public static Status updateClientSavingsByAccountNumberQuery(String an, double savings, JdbcTemplate jdbc) {
        return jdbc.update("UPDATE clients SET savings = ? WHERE account_number = ?", SecurityUtil.encrypt(String.valueOf(savings)), SecurityUtil.encrypt(an)) > 0 ? SUCCESS : ERROR;
    }

    public static Status saveClientQuery(Client client, JdbcTemplate jdbc) {
        final String QUERY = "INSERT INTO clients (account_number, pin, first_name, last_name, gender, address, date_of_birth, savings, attempts) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int result = jdbc.update(
                QUERY,
                SecurityUtil.encrypt(client.accountNumber()),
                SecurityUtil.encrypt(client.pin()),
                SecurityUtil.encrypt(client.details().getFirstName()),
                SecurityUtil.encrypt(client.details().getLastName()),
                client.details().getGender().toString(),
                SecurityUtil.encrypt(client.details().getAddress()),
                client.details().getBirthDate(),
                SecurityUtil.encrypt(String.valueOf(client.savings())),
                client.attempts()
        );
        return result > 0 ? SUCCESS : ERROR;
    }

}
