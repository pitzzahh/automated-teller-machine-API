package io.github.pitzzahh.service;

import java.time.*;
import java.util.List;
import java.util.Map;
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

    private ZonedDateTime NOW = ZonedDateTime.now();

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

    @AfterEach
    void tearDown() {
//        assertEquals(Status.SUCCESS, atmService.removeAllLoans().get());
    }

    @Test
    @Disabled
    void shouldSaveAllClients() {
        var clients = List.of(
                new Client(
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
                ),
                new Client(
                        "143143143",
                        "143143",
                        Person.builder()
                                .firstName("Zamira Alexis")
                                .lastName("Morozuk")
                                .gender(Gender.FEMALE)
                                .address("Earth")
                                .birthDate(LocalDate.of(2009, Month.APRIL, 29))
                                .build(),
                        5_000_000,
                        false
                )
        );
        var result = atmService.saveAllClients().apply(clients);
        assertEquals(Status.SUCCESS, result);
    }

    @Test
    void shouldGetLoanMessage() {
        // given
        var loanNumber = 1;
        var accountNumber = "143143143";
        // when
        var result = atmService.getMessage().apply(loanNumber, accountNumber)
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .toList();
        // then
        result.forEach(System.out::println);
    }

    @Test
    @Disabled
    void shouldGetSavingsByAccountNumber() {
        var client = makeZamira();
        var savings = atmService.getClientSavingsByAccountNumber().apply(client.accountNumber());
        System.out.println("savings = " + savings);
    }

    @Test
    @Disabled
    void shouldMakeALoan() {
        // given
        var client = makePeter();
        var loan = makeLoan(client, 10_000);
        // when
        var result = atmService.requestLoan().apply(loan);
        // then
        assertEquals(Status.SUCCESS, result);
    }

    @Test
    @Disabled
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

    private Client makePeter() {
        return new Client(
                "200263444",
                "143143143",
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

    private Client makeZamira() {
        return new Client(
                "143143143",
                "143143",
                Person.builder()
                        .firstName("Zamira Alexis")
                        .lastName("Morozuk")
                        .gender(Gender.FEMALE)
                        .address("Earth")
                        .birthDate(LocalDate.of(2009, Month.APRIL, 29))
                        .build(),
                5_000_000,
                false
        );
    }

    private Loan makeLoan(Client client, double amount) {
        return new Loan(
                client.accountNumber(),
                LocalDate.now(),
                amount,
                true
        );
    }
}