package io.github.pitzzahh.service;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import org.junit.jupiter.api.*;
import io.github.pitzzahh.atm.dao.AtmDAO;
import io.github.pitzzahh.atm.entity.Loan;
import io.github.pitzzahh.atm.entity.Client;
import com.github.pitzzahh.utilities.Print;
import io.github.pitzzahh.atm.service.AtmService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;
import com.github.pitzzahh.utilities.classes.Person;
import com.github.pitzzahh.utilities.classes.enums.*;
import io.github.pitzzahh.atm.dao.AtmDAOImplementation;
import io.github.pitzzahh.atm.database.DatabaseConnection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AtmServiceTest extends AtmDAOImplementation {

    private AtmDAO atmDAO;

    private AtmService atmService;

    private DatabaseConnection databaseConnection;

    @BeforeEach
    void setUp() {

        atmService = new AtmService(atmDAO, databaseConnection);
        atmService.setDataSource().accept(atmService
                        .connection
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

    @Test
    @Order(7)
    void J_shouldGetLoanMessage() {
        // given
        var accountNumber = makeZamira().accountNumber();
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
    @Order(8)
    void K_shouldGetLoanMessage() {
        // given
        var accountNumber = makePeter().accountNumber();
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
    void shouldThrowExceptionWhenGettingLoanMessagesBecauseClientDoesNotExist() {
        // given
        var accountNumber = "321321321";
        // then
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws IllegalArgumentException {
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
        });
    }

    @Test
    @Order(14)
    void shouldGetSavingsByAccountNumber() {
        var client = makeZamira();
        var savings = atmService.getClientSavingsByAccountNumber().apply(client.accountNumber());
        System.out.println("savings = " + savings);
    }

    @Test
    @Order(15)
    void shouldGetAllLoans() {
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
    @Order(16)
    void shouldThrowExceptionBecauseClientDoesNotExist() {
        // given
        var accountNumber = "123456789";
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws IllegalArgumentException {
                // when
                var client = atmService.getClientByAccountNumber().apply(accountNumber);
                // then
                Print.println(client);
            }
        });
    }
    @Test
    @Order(17)
    void remove() {
        assertEquals(Status.SUCCESS, atmService.removeAllClients().get());
        assertEquals(Status.SUCCESS, atmService.removeAllLoans().get());
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