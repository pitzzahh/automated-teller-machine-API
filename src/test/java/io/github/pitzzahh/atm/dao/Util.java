package io.github.pitzzahh.atm.dao;

import io.github.pitzzahh.util.utilities.classes.enums.Gender;
import io.github.pitzzahh.util.utilities.classes.Person;
import io.github.pitzzahh.atm.entity.Client;
import io.github.pitzzahh.atm.entity.Loan;
import java.time.LocalDate;
import java.time.Month;

public class Util {

    static Client makePeter() {
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


    static Client makeMark() {
        return new Client(
                "143143143",
                "143143",
                Person.builder()
                        .firstName("Mark")
                        .lastName("Silent")
                        .gender(Gender.PREFER_NOT_TO_SAY)
                        .address("Earth")
                        .birthDate(LocalDate.of(2009, Month.APRIL, 29))
                        .build(),
                5_000_000,
                false
        );
    }

    static Loan makeLoan(Client client, double amount) {
        return new Loan(
                client.accountNumber(),
                LocalDate.of(2022, Month.AUGUST, 6),
                amount,
                true
        );
    }
}
