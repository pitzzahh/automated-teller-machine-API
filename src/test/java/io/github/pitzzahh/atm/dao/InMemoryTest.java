package io.github.pitzzahh.atm.dao;

import static io.github.pitzzahh.util.utilities.classes.enums.Status.SUCCESS;
import io.github.pitzzahh.atm.exceptions.ClientNotFoundException;
import static io.github.pitzzahh.util.utilities.Print.println;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import io.github.pitzzahh.atm.exceptions.LoanNotFoundException;
import io.github.pitzzahh.atm.service.AtmService;
import static io.github.pitzzahh.atm.dao.Util.*;
import io.github.pitzzahh.util.utilities.Print;
import io.github.pitzzahh.atm.entity.Loan;
import java.util.stream.Collectors;
import org.junit.jupiter.api.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InMemoryTest {

    private static AtmService atmService;

    @BeforeAll
    static void setUp() {
        atmService = new AtmService(new InMemory());
    }

    @AfterAll
    static void afterAll() {
        assertEquals(SUCCESS, atmService.removeAllLoans().get());
    }

    @Test
    @Order(1)
    void A_shouldSaveAllClients() {
        var clients = List.of(
                makePeter(),
                makeMark()
        );
        var result = atmService.saveAllClients().apply(clients);
        assertEquals(SUCCESS, result);
    }
    @Order(2)
    @Test
    void shouldGetClientByAccountNumber() {
        var apply = atmService.getClientByAccountNumber().apply(makePeter().accountNumber()).orElse(null);
        System.out.println("apply = " + apply);
    }
    @Test
    @Order(3)
    void B_shouldPrintAllClientsBeforeLoanRequests() {
        atmService.getAllClients()
                .get()
                .values()
                .forEach(Print::println);
    }

    @Test
    @Order(4)
    void C_shouldMakeALoan() {
        // given
        var client = makeMark();
        var loan = makeLoan(client, 10_000);
        // when
        var result = atmService.requestLoan().apply(loan);
        // then
        assertEquals(SUCCESS, result);
    }

    @Test()
    @Order(5)
    void D_shouldMakeALoan() {
        // given
        var client = makePeter();
        var loan = makeLoan(client, 10_000);
        // when
        println(loan);
        var result = atmService.requestLoan().apply(loan);
        // then
        assertEquals(SUCCESS, result);
    }

    @Test
    @Order(6)
    void E_shouldApproveLoan() {

        var client = makeMark();
        var loan = atmService.getAllLoans()
                .get()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .filter(l -> l.accountNumber().equals(client.accountNumber()) && l.pending())
                .findAny()
                .orElseThrow(LoanNotFoundException::new);

        var result = atmService.approveLoan().apply(loan, client);
        assertEquals(SUCCESS, result);
    }

    @Test
    @Order(7)
    void F_shouldApproveLoan() {

        var client = makePeter();
        var loan = atmService.getAllLoans()
                .get()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .filter(l -> l.accountNumber().equals(client.accountNumber()) && l.pending())
                .findAny()
                .orElseThrow(LoanNotFoundException::new);


        var result = atmService.approveLoan().apply(loan, client);
        assertEquals(SUCCESS, result);
    }

    @Test
    @Order(8)
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
                .collect(Collectors.toList());
        // then
        result.forEach(Print::println);
    }

    @Test
    @Order(9)
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
                .collect(Collectors.toList());
        // then
        result.forEach(Print::println);
    }

    @Test
    @Order(10)
    void G_shouldPrintAllClientsAfterLoanRequests() {
        atmService.getAllClients()
                .get()
                .values()
                .forEach(Print::println);
    }

    @Test
    @Order(11)
    void shouldDeclineLoan() {
        var client = makePeter();
        var loan = new Loan(
                1,
                client.accountNumber(),
                true
        );
        var result = atmService.declineLoan().apply(loan);
        assertEquals(SUCCESS, result);
    }

    @Test
    @Order(12)
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
                .collect(Collectors.toList());
        // then
        result.forEach(Print::println);
    }

    @Test
    @Order(13)
    void G_shouldPrintAllClientsAfterLoanRequests2() {
        atmService.getAllClients()
                .get()
                .values()
                .forEach(Print::println);
    }

    @Test
    @Order(14)
    void shouldThrowExceptionWhenGettingLoanMessagesBecauseClientDoesNotExist() {
        // given
        var accountNumber = "321321321";
        // then
        assertThrows(ClientNotFoundException.class, () -> {
            // when
            var result = atmService.getMessage().apply(accountNumber)
                    .entrySet()
                    .stream()
                    .filter(e -> e.getKey().equals(accountNumber))
                    .map(Map.Entry::getValue)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            // then
            result.forEach(Print::println);
        });
    }

    @Test
    @Order(15)
    void shouldGetSavingsByAccountNumber() {
        var client = makeMark();
        var savings = atmService.getClientSavingsByAccountNumber().apply(client.accountNumber());
        println("savings = " + savings);
    }

    @Test
    @Order(16)
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
    @Order(17)
    void shouldThrowExceptionBecauseClientDoesNotExist() {
        // given
        var accountNumber = "123456789";
        assertThrows(ClientNotFoundException.class, () -> atmService.getClientByAccountNumber().apply(accountNumber));
    }

}