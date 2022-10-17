package io.github.pitzzahh.atm.dao;

import java.util.Map;
import java.util.List;
import java.util.Collection;
import org.junit.jupiter.api.*;
import io.github.pitzzahh.atm.entity.Loan;
import io.github.pitzzahh.util.utilities.Print;
import static io.github.pitzzahh.atm.dao.Util.*;
import io.github.pitzzahh.atm.service.AtmService;
import static org.junit.jupiter.api.Assertions.*;
import io.github.pitzzahh.util.utilities.classes.enums.*;
import io.github.pitzzahh.atm.database.DatabaseConnection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InDatabaseTest extends InDatabase {


    private static AtmService atmService;

    private static final DatabaseConnection DATABASE_CONNECTION = new DatabaseConnection();

    @BeforeEach
    void setUp() {
        atmService = new AtmService(new InDatabase());
        atmService.setDataSource().accept(
                DATABASE_CONNECTION
                        .setDriverClassName("org.postgresql.Driver")
                        .setUrl("jdbc:postgresql://localhost:5432/postgres")
                        .setUsername("postgres")
                        .setPassword("!P4ssW0rd@123")
                        .getDataSource()
        );
    }

    @AfterAll
    static void afterAll() {
        assertEquals(Status.SUCCESS, atmService.removeAllClients().get());
        assertEquals(Status.SUCCESS, atmService.removeAllLoans().get());
    }

    @Test
    @Order(1)
    void A_shouldSaveAllClients() {
        var clients = List.of(
                makePeter(),
                makeMark()
        );
        var result = atmService.saveAllClients().apply(clients);
        assertEquals(Status.SUCCESS, result);
    }

    @Test
    @Order(2)
    void B_shouldPrintAllClientsBeforeLoanRequests() {
        atmService.getAllClients()
                .get()
                .values()
                .forEach(Print::println);
    }

    @Test
    @Order(3)
    void C_shouldMakeALoan() {
        // given
        var client = makeMark();
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

        var client = makeMark();
        var loan = atmService.getAllLoans()
                .get()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .filter(l -> l.accountNumber().equals(client.accountNumber()) && l.pending())
                .findAny()
                .orElseThrow(IllegalStateException::new);

        var result = atmService.approveLoan().apply(loan, client);
        assertEquals(Status.SUCCESS, result);
    }

    @RepeatedTest(2)
    @Order(6)
    void F_shouldApproveLoan() {

        var client = makePeter();
        var loan = atmService.getAllLoans()
                .get()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .filter(l -> l.accountNumber().equals(client.accountNumber()) && l.pending())
                .findAny()
                .orElseThrow(IllegalStateException::new);


        var result = atmService.approveLoan().apply(loan, client);
        assertEquals(Status.SUCCESS, result);
    }

    @Test
    @Order(7)
    void J_shouldGetLoanMessage() {
        // given
        var accountNumber = makeMark().accountNumber();
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
                .values()
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
                .values()
                .forEach(Print::println);
    }

    @Test
    @Order(13)
    void shouldThrowExceptionWhenGettingLoanMessagesBecauseClientDoesNotExist() {
        // given
        var accountNumber = "321321321";
        // then
        assertThrows(IllegalArgumentException.class, () -> {
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
        });
    }

    @Test
    @Order(14)
    void shouldGetSavingsByAccountNumber() {
        var client = makeMark();
        var savings = atmService.getClientSavingsByAccountNumber().apply(client.accountNumber());
        System.out.println("savings = " + savings);
    }

    @Test
    @Order(15)
    void shouldGetAllLoans() {
        // given
        var loan = atmService.getAllLoans();
        // when
        loan.get().values()
                .stream()
                .flatMap(Collection::stream)
                .forEach(Print::println);
    }

    @Test
    @Order(16)
    void shouldThrowExceptionBecauseClientDoesNotExist() {
        // given
        var accountNumber = "123456789";
        assertThrows(IllegalArgumentException.class, () -> {
            // when
            var client = atmService.getClientByAccountNumber().apply(accountNumber);
            // then
            Print.println(client);
        });
    }

}