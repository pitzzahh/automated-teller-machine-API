package io.github.pitzzahh.atm.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import io.github.pitzzahh.atm.entity.Loan;
import org.springframework.jdbc.core.RowMapper;
import com.github.pitzzahh.utilities.SecurityUtil;

/**
 * Used to map loan history row from the database to a {@code Loan} object.
 */
public class LoanMapper implements RowMapper<Loan> {

    /**
     * maps the data from the table to a {@code Loan} object.
     * @param resultSet the ResultSet to map (pre-initialized for the current row)
     * @param numberOfRow the number of the current row
     * @return {@code Loan} object.
     * @throws SQLException if something is went wrong.
     */
    @Override
    public Loan mapRow(ResultSet resultSet, int numberOfRow) throws SQLException {
        return new Loan(
                resultSet.getInt("loan_number"),
                SecurityUtil.decrypt(resultSet.getString("account_number")),
                resultSet.getDate("date_of_loan").toLocalDate(),
                Double.parseDouble(SecurityUtil.decrypt(resultSet.getString("amount"))),
                Boolean.valueOf(SecurityUtil.decrypt(resultSet.getString("pending"))),
                Boolean.valueOf(SecurityUtil.decrypt(resultSet.getString("declined")))
        );
    }
}
