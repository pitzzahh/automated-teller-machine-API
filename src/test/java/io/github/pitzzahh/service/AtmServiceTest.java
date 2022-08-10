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
import org.junit.jupiter.api.function.Executable;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    @Test
    @Order(1)
    void A_shouldSaveAllClients() {
        var clients = List.of(
                makePeter(),
                makeZamira()
        );
        var result = atmService.saveAllClients().apply(clients);
        assertEquals(Status.SUCCESS, result);
    }

    @Test
    @Order(2)
    void B_shouldPrintAllClientsBeforeLoanRequests() {
        atmService.getAllClients()
                .get()
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .forEach(Print::println);
    }

    @Disabled
    @Test
    @Order(3)
    void C_shouldMakeALoan() {
        // given
        var client = makeZamira();
        for (int i = 10_000; i <= 20_000; i += 10_000) {
            var loan = makeLoan(client, i);
            // when
            var result = atmService.requestLoan().apply(loan);
            // then
            assertEquals(Status.SUCCESS, result);
        }
    }

    @Test()
    @Order(4)
    void D_shouldMakeALoan() {
        // given
        var client = makePeter();
        for (int i = 10_000; i <= 20_000; i += 10_000) {
            var loan = makeLoan(client, i);
            // when
            var result = atmService.requestLoan().apply(loan);
            // then
            assertEquals(Status.SUCCESS, result);
        }
    }

    @Disabled
    @RepeatedTest(2)
    @Order(5)
    void E_shouldApproveLoan() {

        var client = makeZamira();
        var loan = atmService.getAllLoans()
                .get()
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .filter(l -> l.accountNumber().equals(client.accountNumber()) && l.pending())
                .findAny()
                .get();

        var result = atmService.approveLoan().apply(loan, client);
        assertEquals(Status.SUCCESS, result);
    }

    @Disabled
    @RepeatedTest(2)
    @Order(6)
    void F_shouldApproveLoan() {

        var client = makePeter();
        var loan = atmService.getAllLoans()
                .get()
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .filter(l -> l.accountNumber().equals(client.accountNumber()) && l.pending())
                .findAny()
                .get();

        var result = atmService.approveLoan().apply(loan, client);
        assertEquals(Status.SUCCESS, result);
    }

    @Disabled
    @Test
    @Order(7)
    void J_shouldGetLoanMessage() {
        // given
        var accountNumber = "143143143";
        // when
        var result = atmService.getMessage().apply(accountNumber)
                .entrySet()
                .stream()
                .filter(e -> e.getKey().equals(accountNumber))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .toList();
        // then
        result.forEach(Print::println);
    }

    @Disabled
    @Test
    @Order(8)
    void K_shouldGetLoanMessage() {
        // given
        var accountNumber = "200263444";
        // when
        var result = atmService.getMessage().apply(accountNumber)
                .entrySet()
                .stream()
                .filter(e -> e.getKey().equals(accountNumber))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .toList();
        // then
        result.forEach(Print::println);
    }

    @Test
    @Order(9)
    void G_shouldPrintAllClientsAfterLoanRequests() {
        atmService.getAllClients()
                .get()
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .forEach(Print::println);
    }

    @Test
    @Order(10)
    void shouldDeclineLoan() {
        var client = makePeter();
        var loan = new Loan(
                1,
                client.accountNumber(),
                true
        );
        var result = atmService.declineLoan().apply(loan);
        assertEquals(Status.SUCCESS, result);
    }

    @Test
    @Order(11)
    void K_shouldGetLoanMessage2() {
        // given
        var accountNumber = "123123123";
        // when
        var result = atmService.getMessage().apply(accountNumber)
                .entrySet()
                .stream()
                .filter(e -> e.getKey().equals(accountNumber))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .toList();
        // then
        result.forEach(Print::println);
    }

    @Test
    @Order(12)
    void G_shouldPrintAllClientsAfterLoanRequests2() {
        atmService.getAllClients()
                .get()
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .forEach(Print::println);
    }

    @Test
    @Order(13)
    void remove() {
        assertEquals(Status.SUCCESS, atmService.removeAllClients().get());
        assertEquals(Status.SUCCESS, atmService.removeAllLoans().get());
    }

    @Test
    @Disabled
    void shouldThrowExceptionWhenGettingLoanMessageBecauseLoanMessageDoesNotExist() {
        // given
        var accountNumber = "123123123";
        // then
        assertThrows(IllegalStateException.class, new Executable() {
            @Override
            public void execute() throws IllegalStateException {
                // when
                var result = atmService.getMessage().apply(accountNumber)
                        .entrySet()
                        .stream()
                        .filter(e -> e.getKey().equals(accountNumber))
                        .map(Map.Entry::getValue)
                        .flatMap(Collection::stream)
                        .toList();
                // then
                result.forEach(System.out::println);
            }
        });
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

    private Client makePeter() {
        return new Client(
                "123123123",
                "123123",
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
                LocalDate.of(2022, Month.AUGUST, 6),
                amount,
                true
        );
    }
}