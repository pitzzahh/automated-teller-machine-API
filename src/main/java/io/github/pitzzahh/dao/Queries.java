package io.github.pitzzahh.dao;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import io.github.pitzzahh.entity.Client;
import io.github.pitzzahh.mapper.ClientMapper;
import com.github.pitzzahh.utilities.SecurityUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import com.github.pitzzahh.utilities.classes.enums.Status;
import org.springframework.dao.EmptyResultDataAccessException;
import static com.github.pitzzahh.utilities.classes.enums.Status.*;

public class Queries {

    public static Supplier<Map<String, Client>> getAllClientsQuery(JdbcTemplate jdbc) {
        return () -> jdbc.query("SELECT * FROM clients", new ClientMapper()).stream().collect(Collectors.toMap(Client::accountNumber, Function.identity()));
    }

    public static Optional<Client> getClientByAccountNumberQuery(String $an, JdbcTemplate jdbc) {
        try {
            return Optional.ofNullable(jdbc.queryForObject("SELECT * FROM clients WHERE account_number = ?", new ClientMapper(), SecurityUtil.encrypt($an)));
        } catch (EmptyResultDataAccessException ignored) {}
        return Optional.empty();
    }

    public static Status removeClientByAccountNumberQuery(String $an, JdbcTemplate jdbc) {
        return jdbc.update("DELETE FROM clients WHERE account_number = ?", SecurityUtil.encrypt($an)) > 0 ? SUCCESS : ERROR;
    }

    public static Status updateClientStatusByAccountNumber(String an, boolean status, JdbcTemplate jdbc) {
        return jdbc.update("UPDATE clients SET isLocked = ? WHERE account_number = ?", status, SecurityUtil.encrypt(an)) > 0 ? SUCCESS : ERROR;

    }

    public static Status updateClientSavingsByAccountNumberQuery(String an, double savings, JdbcTemplate jdbc) {
        return jdbc.update("UPDATE clients SET savings = ? WHERE account_number = ?", SecurityUtil.encrypt(String.valueOf(savings)), SecurityUtil.encrypt(an)) > 0 ? SUCCESS : ERROR;
    }

    public static Status saveClientQuery(Client client, JdbcTemplate jdbc) {
        final String QUERY = "INSERT INTO clients (account_number, pin, first_name, last_name, gender, address, date_of_birth, savings, isLocked) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                client.isLocked()
        );
        return result > 0 ? SUCCESS : ERROR;
    }

}
