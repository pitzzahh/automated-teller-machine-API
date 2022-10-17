package io.github.pitzzahh.atm.dao;

import static io.github.pitzzahh.util.utilities.classes.enums.Status.ERROR;
import static io.github.pitzzahh.util.utilities.classes.enums.Status.SUCCESS;
import io.github.pitzzahh.atm.exceptions.ClientNotFoundException;
import io.github.pitzzahh.util.utilities.classes.enums.Status;
import io.github.pitzzahh.atm.entity.Client;
import io.github.pitzzahh.atm.entity.Loan;
import io.github.pitzzahh.atm.dao.AtmDAO;
import static java.lang.String.format;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.*;

public class InMemory implements AtmDAO {

    private final Map<String, Client> CLIENTS = new HashMap<>();
    private final Map<String, Loan> LOANS = new HashMap<>();

    @Override
    public Supplier<Map<String, Client>> getAllClients() {
        return () -> CLIENTS;
    }

    @Override
    public Function<String, Client> getClientByAccountNumber() throws IllegalArgumentException {
        return accountNumber -> CLIENTS.entrySet()
                .stream()
                .filter(e -> e.getKey().equals(accountNumber))
                .findAny()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new ClientNotFoundException(format("Client with account number [%s] does not exist", accountNumber)));
    }

    @Override
    public Function<String, Double> getClientSavingsByAccountNumber() {
        return accountNumber -> getClientByAccountNumber().apply(accountNumber).savings();
    }

    @Override
    public Function<String, Status> removeClientByAccountNumber() {
        return accountNumber -> {
            var isPresent = CLIENTS.containsKey(accountNumber);
            if (isPresent) CLIENTS.remove(accountNumber);
            else throw new ClientNotFoundException(format("Client with account number [%s] does not exist", accountNumber));
            return SUCCESS;
        };
    }

    @Override
    public Supplier<Status> removeAllClients() {
        CLIENTS.clear();
        return () -> CLIENTS.isEmpty() ? SUCCESS : ERROR;
    }

    @Override
    public BiFunction<String, Boolean, Status> updateClientStatusByAccountNumber() {
        return (accountNumber, status) -> {
            var client = getClientByAccountNumber().apply(accountNumber);
            client.setLocked(status);
            return client.isLocked() == status? SUCCESS : ERROR;
        };
    }

    @Override
    public BiFunction<String, Double, Status> updateClientSavingsByAccountNumber() {
        return (accountNumber, savings) -> {
            var client = getClientByAccountNumber().apply(accountNumber);
            client.setSavings(savings);
            return client.savings() == savings ? SUCCESS : ERROR;
        };
    }

    @Override
    public Function<Client, Status> saveClient() {
        return client -> CLIENTS.put(client.accountNumber(), client) == client ? SUCCESS : ERROR;
    }

    @Override
    public Function<Collection<Client>, Status> saveAllClients() {
        return clients -> {
            CLIENTS.putAll(
                    clients.stream()
                            .collect(Collectors.toMap(Client::accountNumber, Function.identity()))
            );
            return SUCCESS;
        };
    }

    @Override
    public Function<Loan, Status> requestLoan() {
        return loan -> LOANS.put(loan.accountNumber(), loan) == loan ? SUCCESS : ERROR;
    }

    @Override
    public Supplier<Map<String, List<Loan>>> getAllLoans() {
        return () -> LOANS.values()
                .stream()
                .collect(Collectors.groupingBy(Loan::accountNumber));
    }

    @Override
    public BiFunction<Integer, String, Optional<Loan>> getLoanByLoanNumberAndAccountNumber() {
        return (loanNumber, accountNumber) -> LOANS.values()
                .stream()
                .filter(loan -> loan.loanNumber() == loanNumber && loan.accountNumber().equals(accountNumber))
                .findAny();
    }

    @Override
    public Function<String, Integer> getLoanCount() {
        return accountNumber -> {
            int result = LOANS.values()
                    .stream()
                    .filter(loan -> loan.accountNumber().equals(accountNumber))
                    .map(Loan::loanNumber)
                    .max(Comparator.comparingInt(Integer::intValue))
                    .orElse(0);
            return result >= 1 ? result + 1 : 1;
        };
    }

    @Override
    public BiFunction<Loan, Client, Status> approveLoan() {
        return (loan, client) -> {
            var c = getClientByAccountNumber().apply(client.accountNumber());
            var status = updateClientSavingsByAccountNumber().apply(c.accountNumber(), c.savings() + loan.amount());
            LOANS.get(loan.accountNumber()).setPending(false);
            return status;
        };
    }

    @Override
    public Function<Loan, Status> declineLoan() {
        return loan -> {
            LOANS.get(loan.accountNumber()).setDeclined(true);
            return LOANS.get(loan.accountNumber()).isDeclined() ? SUCCESS : ERROR;
        };
    }

    @Override
    public Function<Loan, Status> removeLoan() {
        return loan -> loan.equals(LOANS.remove(loan.accountNumber())) ? SUCCESS : ERROR;
    }

    @Override
    public Supplier<Status> removeAllLoans() {
        LOANS.clear();
        return () -> LOANS.isEmpty() ? SUCCESS : ERROR;
    }

}
