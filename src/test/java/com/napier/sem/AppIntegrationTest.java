package com.napier.sem;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppIntegrationTest {

    private App app;

    @BeforeEach
    void setup() {
        app = new App();
    }

    /**
     * Helper method: check if database is reachable.
     * This prevents CI from failing if MySQL is not running.
     */
    private boolean canConnectToDatabase() {
        try {
            DriverManager.getConnection(
                    "jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "example"
            ).close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Test
    @Order(1)
    void testDatabaseConnection() {
        Assumptions.assumeTrue(canConnectToDatabase(),
                "Skipping integration test: MySQL is not available");

        app.connect();
        // Connection should not be null after successful connect
        assertNotNull(app.con, "Connection should not be null for integration test");

        app.disconnect();
    }
}

