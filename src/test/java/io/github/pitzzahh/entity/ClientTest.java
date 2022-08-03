package io.github.pitzzahh.entity;

import com.github.pitzzahh.utilities.classes.Person;
import com.github.pitzzahh.utilities.classes.enums.Gender;
import com.github.pitzzahh.utilities.classes.enums.Status;
import io.github.pitzzahh.dao.AtmDAO;
import io.github.pitzzahh.dao.AtmDAOImplementation;
import io.github.pitzzahh.database.DatabaseConnection;
import io.github.pitzzahh.service.AtmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest extends AtmDAOImplementation {

    private AtmDAO atmDAO;
    private AtmService atmService;
    private DatabaseConnection databaseConnection;

    @BeforeEach
    void setUp() {
        atmService = new AtmService(atmDAO);
        databaseConnection = new DatabaseConnection();
        atmService.setDataSource()
                .accept(databaseConnection
                        .setDriverClassName("org.postgresql.Driver")
                        .setUrl("jdbc:postgresql://localhost/atm")
                        .setUsername("postgres")
                        .setPassword("!Password123")
                        .getDataSource()
                );
    }

    @Test
    void a_shouldAddClient() {
        var client = new Client(
                "777777777",
                "555555",
                new Person(
                        "Mark",
                        "Silent",
                        Gender.PREFER_NOT_TO_SAY,
                        "Earth",
                        LocalDate.of(2001, Month.OCTOBER, 10)
                ),
                99999,
                false
        );
        var result = atmService.saveClient().apply(client);
        assertEquals(Status.SUCCESS, result);
    }

    @Test
    void b_shouldGetClient() {
        var client = atmService.getAllClients()
                .get()
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .toList();
        client.forEach(System.out::println);

    }
}