package ru.cft.template.testContainers;


import org.junit.AfterClass;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class PostgreSQLTestContainer {
    @Container
    private static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:12")
            .withDatabaseName("walletTests")
            .withUsername("walletTests")
            .withPassword("walletTests");

    public static PostgreSQLContainer getInstance() {
        return postgres;
    }

    @AfterClass
    public static void stopContainer() {
        postgres.stop();
    }

    @Test
    public void test() {
        assertTrue(postgres.isRunning());
    }
}
