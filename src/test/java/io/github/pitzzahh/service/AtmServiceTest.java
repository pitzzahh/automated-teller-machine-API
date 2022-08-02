package io.github.pitzzahh.service;

import java.util.Map;
import java.time.Month;
import java.time.LocalDate;

import org.junit.jupiter.api.*;
import io.github.pitzzahh.dao.AtmDAO;
import io.github.pitzzahh.entity.Loan;
import io.github.pitzzahh.entity.Client;
import com.github.pitzzahh.utilities.Print;
import static org.junit.jupiter.api.Assertions.*;
import io.github.pitzzahh.dao.AtmDAOImplementation;
import com.github.pitzzahh.utilities.classes.Person;
import com.github.pitzzahh.utilities.classes.enums.*;
import io.github.pitzzahh.database.DatabaseConnection;

class AtmServiceTest extends AtmDAOImplementation {

    private AtmDAO atmDAO;
    private AtmService atmService;

    @BeforeEach
    void setUp() {
        atmService = new AtmService(atmDAO);
    }

    @AfterEach
    @Disabled
    void tearDown() {
        assertEquals(Status.ERROR, atmService.removeAllClients().get());
        assertEquals(Status.SUCCESS, atmService.removeAllLoans().get());
    }

    @RepeatedTest(10)
    @Disabled
    void shouldMakeALoan() {
        // given
        var client = makeClient();
        var loan = makeLoan(client);
        // when
        var result = atmService.requestLoan().apply(loan);

        // then
        assertEquals(Status.SUCCESS, result);
    }

    @Test
    void shouldMetTheLoan() {
        // given
        var loan = atmService.getAllLoans();
        // when
        loan.get().entrySet().stream().map(Map.Entry::getValue).forEach(Print::println);
    }

    private static Client makeClient() {
        return new Client(
                "200263444",
                "555555",
                Person.builder()
                        .firstName("Peter John")
                        .lastName("Arao")
                        .gender(Gender.MALE)
                        .address("Earth")
                        .birthDate(LocalDate.of(2002, Month.AUGUST, 24))
                        .build(),
                5_000_000,
                false
        );
    }

    private static Loan makeLoan(Client client) {
        return new Loan(
                client.accountNumber(),
                LocalDate.now(),
                10_000,
                true
        );
    }
}