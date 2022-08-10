package io.github.pitzzahh.automated_teller_machine.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.github.pitzzahh.utilities.SecurityUtil;
import com.github.pitzzahh.utilities.classes.Person;
import com.github.pitzzahh.utilities.classes.enums.Gender;
import io.github.pitzzahh.automated_teller_machine.entity.Client;

/**
 * Class used to map data from the table to a {@code Client} object
 */
public class ClientMapper implements RowMapper<Client> {

    /**
     * maps the data from the table to a {@code Client} object.
     * @param resultSet the ResultSet to map (pre-initialized for the current row)
     * @param numberOfRow the number of the current row
     * @return {@code Client} object.
     * @throws SQLException if something is went wrong.
     */
    @Override
    public Client mapRow(ResultSet resultSet, int numberOfRow) throws SQLException {
        return new Client(
                SecurityUtil.decrypt(resultSet.getString("account_number")),
                SecurityUtil.decrypt(resultSet.getString("pin")),
                new Person(
                        SecurityUtil.decrypt(resultSet.getString("first_name")),
                        SecurityUtil.decrypt(resultSet.getString("last_name")),
                        Gender.valueOf(resultSet.getString("gender")),
                        SecurityUtil.decrypt(resultSet.getString("address")),
                        resultSet.getDate("date_of_birth").toLocalDate()
                ),
                Double.parseDouble(SecurityUtil.decrypt(resultSet.getString("savings"))),
                resultSet.getBoolean("isLocked")
        );
    }
}
