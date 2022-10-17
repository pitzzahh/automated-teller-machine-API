package io.github.pitzzahh.atm.dao;

import io.github.pitzzahh.atm.dao.InMemory;
import io.github.pitzzahh.atm.service.AtmService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeEach;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InMemoryAtmServiceTest {

    private static AtmService atmService;

    @BeforeEach
    void setUp() {
        atmService = new AtmService(new InMemory());
    }

    @AfterAll
    static void afterAll() {
        atmService.removeAllClients();
    }
}