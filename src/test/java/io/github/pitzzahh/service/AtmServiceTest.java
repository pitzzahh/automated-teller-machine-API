package io.github.pitzzahh.service;

import java.util.Map;
import java.time.Month;
import java.time.LocalDate;
import java.util.Collection;
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

    @RepeatedTest(3)
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
        loan.get().entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .forEach(Print::println);
    }

    @Test
    @Disabled
    void shouldApproveLoan() {

        var loan = atmService
                .getAllLoans()
                .get()
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .findAny()
                .get();

        var l = new Loan(
                loan.loanNumber(),
                loan.accountNumber(),
                false
        );

        var result = atmService.approveLoan().apply(l);
        assertEquals(Status.SUCCESS, result);
    }

    @Test
    @Disabled
    void shouldApproveAllLoans() {
        var loans = atmService
                .getAllLoans()
                .get()
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .toList();

        loans.forEach(l -> atmService.approveLoan().apply(l));

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